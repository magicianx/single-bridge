package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.RedisConstant;
import com.gaea.single.bridge.converter.UserGreetConverter;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.manager.AbstractCache;
import com.gaea.single.bridge.core.manager.GreetUserManager;
import com.gaea.single.bridge.core.manager.UserManager;
import com.gaea.single.bridge.core.manager.model.GreetInfo;
import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.GreetStatusRes;
import com.gaea.single.bridge.dto.user.SendGreetUserMessageRes;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import com.gaea.single.bridge.entity.mongodb.SystemGreetMessage;
import com.gaea.single.bridge.entity.mongodb.UserGreetConfig;
import com.gaea.single.bridge.entity.mysql.model.UserBaseInfo;
import com.gaea.single.bridge.enums.AnchorAuthStatus;
import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.repository.mongodb.SystemGreetMessageRepository;
import com.gaea.single.bridge.repository.mongodb.UserGreetConfigRepository;
import com.gaea.single.bridge.repository.mongodb.UserRepository;
import com.gaea.single.bridge.repository.mysql.UserCompositeRepository;
import com.gaea.single.bridge.repository.mysql.UserSocialInfoRepository;
import com.gaea.single.bridge.service.UserGreetService;
import com.gaea.single.bridge.util.LoboUtil;
import com.gaea.single.bridge.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RBucketReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/** @author cludy */
@Service
@Slf4j
public class UserGreetServiceImpl extends AbstractCache implements UserGreetService {
  @Autowired private UserRepository userRepository;
  @Autowired private UserGreetConfigRepository userGreetConfigRepository;
  @Autowired private SystemGreetMessageRepository systemGreetMessageRepository;
  @Autowired private UserSocialInfoRepository userSocialInfoRepository;
  @Autowired private GreetUserManager greetUserManager;
  @Autowired private UserManager userManager;
  @Autowired private UserCompositeRepository userCompositeRepository;

  @Override
  public Mono<UserGreetConfigRes> getGreetConfig(Long userId) {
    Mono<List<SystemGreetMessage>> allMessageMono =
        systemGreetMessageRepository.findAll().collectList();
    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(config -> allMessageMono.map(messages -> Pair.of(config, messages)))
        .map(UserGreetConverter.toUserGreetConfigRes::convert);
  }

  @Override
  public Mono<Void> initGreetConfig(Long userId) {
    Mono<List<SystemGreetMessage>> allMessageMono =
        systemGreetMessageRepository.findAll().collectList();
    return userGreetConfigRepository
        .countByUserId(userId)
        .flatMap(
            count -> {
              if (count == 0) {
                log.info("正在初始化用户{}打招呼消息配置", userId);
                // 不存在则创建
                return allMessageMono.flatMap(
                    systemMessages -> {
                      List<String> systemIds =
                          systemMessages.stream()
                              .map(SystemGreetMessage::getId)
                              .collect(Collectors.toList());

                      UserGreetConfig config = new UserGreetConfig();
                      config.setUserId(userId);
                      config.setCustomMessages(Collections.emptyList());
                      config.setSystemMessageIds(systemIds);
                      return userGreetConfigRepository.save(config).then();
                    });
              }
              return Mono.empty();
            });
  }

  @Override
  public Mono<GreetMessageRes> addCustomGreetMessage(Long userId, String content) {
    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(
            config ->
                systemGreetMessageRepository
                    .count()
                    .flatMap(
                        count -> {
                          if (config.getCustomMessages().size() + count
                              == DictionaryProperties.get()
                                  .getGreetMessage()
                                  .getMaxMessageCount()) {
                            return Mono.error(
                                ErrorCode.CUSTOM_GREET_MESSAGE_LIMIT_COUNT.newBusinessException());
                          }
                          UserGreetConfig.Message message =
                              new UserGreetConfig.Message(StringUtil.uuid(), content);
                          config.getCustomMessages().add(0, message);
                          return userGreetConfigRepository.save(config).thenReturn(message);
                        }))
        .map(message -> new GreetMessageRes(message.getId(), true, message.getContent()));
  }

  @Override
  public Mono<Void> deleteCustomMessage(Long userId, String messageId) {
    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(
            config -> {
              List<UserGreetConfig.Message> messages =
                  config.getCustomMessages().stream()
                      .filter(message -> !message.getId().equals(messageId))
                      .collect(Collectors.toList());
              config.setCustomMessages(messages);
              return userGreetConfigRepository.save(config).then();
            });
  }

  @Override
  public Mono<GreetStatusRes> getGreetStatus(Long userId) {
    return userManager
        .getUser(userId)
        .flatMap(
            user -> {
              RBucketReactive<GreetInfo> greetInfoBucket =
                  singleRedission.getBucket(key(RedisConstant.USER_GREET_INFO, userId));

              return greetInfoBucket
                  .get()
                  .map(
                      info -> {
                        // 距离上次发送的间隔秒数
                        long intervalSecond =
                            ChronoUnit.SECONDS.between(
                                info.getLastGreetTime(), LocalDateTime.now());
                        // 距离下次发送的剩余秒数
                        long lastSecond =
                            DictionaryProperties.get().getGreetMessage().getSendIntervalSecond()
                                - intervalSecond;
                        lastSecond = lastSecond < 0 ? 0 : lastSecond;
                        log.info("用户{}距离下次发送打招呼消息剩余{}秒", userId, lastSecond);
                        return new GreetStatusRes(user.getIsEnableGreet(), (int) lastSecond);
                      })
                  .switchIfEmpty(
                      Mono.defer(() -> Mono.just(new GreetStatusRes(user.getIsEnableGreet(), 0))));
            });
  }

  @Override
  public Mono<Void> useSystemGreetMessage(Long userId, String messageId, boolean isUse) {
    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(
            config -> {
              // 使用
              if (isUse) {
                boolean notExist =
                    config.getSystemMessageIds().stream().noneMatch(id -> id.equals(messageId));
                if (notExist) {
                  config.getSystemMessageIds().add(messageId);
                  return userGreetConfigRepository.save(config).then();
                }
                // 取消使用
              } else {
                List<String> messageIds =
                    config.getSystemMessageIds().stream()
                        .filter(id -> !id.equals(messageId))
                        .collect(Collectors.toList());
                config.setSystemMessageIds(messageIds);
                return userGreetConfigRepository.save(config).then();
              }

              return Mono.empty();
            });
  }

  @Override
  public Mono<SendGreetUserMessageRes> sendGreetMessage(Long userId) {
    log.info("用户{}发送打招呼消息", userId);
    DictionaryProperties.GreetMessage greetConfig = DictionaryProperties.get().getGreetMessage();

    return validateIsGreetable(userId, greetConfig)
        .flatMap(
            greetInfo ->
                getGreetMessages(userId)
                    .flatMap(
                        messages ->
                            greetUserManager
                                .getGreetUsers(userId, greetConfig.getOneMaxSendGreetCount())
                                .flatMap(
                                    greetUserIds -> {
                                      if (greetUserIds.isEmpty()) {
                                        return Mono.error(
                                            ErrorCode.UNAVAILABLE_GREET_USER
                                                .newBusinessException());
                                      }
                                      return getGreetUserMessages(userId, greetUserIds, messages)
                                          .flatMap(
                                              v ->
                                                  singleRedission
                                                      .getBucket(
                                                          key(
                                                              RedisConstant.USER_GREET_INFO,
                                                              userId))
                                                      .set(
                                                          new GreetInfo(
                                                              greetInfo.getGreetTimes() + 1,
                                                              LocalDateTime.now()))
                                                      .thenReturn(
                                                          new SendGreetUserMessageRes(
                                                              greetConfig.getSendIntervalSecond(),
                                                              v)));
                                    })
                                .switchIfEmpty(
                                    Mono.defer(
                                        () ->
                                            Mono.error(
                                                ErrorCode.UNAVAILABLE_GREET_USER
                                                    .newBusinessException())))));
  }

  @Override
  public Mono<Void> setGreetStatus(Long userId, boolean isEnable) {
    return userManager
        .getUser(userId)
        .flatMap(
            user -> {
              user.setIsEnablePosition(isEnable);
              return userRepository.save(user).then();
            });
  }

  @Override
  public Mono<Void> addGreetUser(Long userId, boolean isNew) {
    if (isNew) {
      return greetUserManager.addGreetUser(userId, isNew);
    } else {
      return userManager
          .getUserGender(userId)
          .flatMap(
              genderType -> {
                if (GenderType.MALE == genderType) {
                  return userSocialInfoRepository
                      .findByUserId(userId)
                      .flatMap(
                          s -> {
                            // 主播不加入队列
                            if (AnchorAuthStatus.ofCode(s.getIsVideoAudit()).isAuditPass()) {
                              log.info("用户{}为主播，不加入打招呼用户队列", userId);
                              return Mono.empty();
                            }
                            return greetUserManager.addGreetUser(userId, isNew);
                          });
                }
                log.info("用户{}为女性，不加入打招呼用户队列", userId);
                return Mono.empty();
              });
    }
  }

  @Override
  public Mono<Void> removeGreetUser(Long userId) {
    return greetUserManager.removeGreetUser(userId);
  }

  private Mono<List<SendGreetUserMessageRes.GreetUserMessage>> getGreetUserMessages(
      Long userId, Set<Long> greetUserIds, List<String> messages) {
    log.info(
        "用户{}发送打招呼消息给用户: {}",
        userId,
        greetUserIds.stream().map(Object::toString).collect(Collectors.joining(",")));
    return userCompositeRepository
        .listBaseInfoByUserIds(greetUserIds)
        .collectList()
        .flatMap(
            baseInfos -> {
              List<Mono<SendGreetUserMessageRes.GreetUserMessage>> monos = new ArrayList<>();
              int messageIndex = 0;
              for (UserBaseInfo baseInfo : baseInfos) {
                if (messageIndex == messages.size()) {
                  messageIndex = 0;
                }
                String message = messages.get(messageIndex++);
                monos.add(
                    userManager
                        .isVip(baseInfo.getUserId())
                        .map(
                            isVip -> {
                              SendGreetUserMessageRes.GreetUserMessage greetUserMessage =
                                  new SendGreetUserMessageRes.GreetUserMessage();
                              greetUserMessage.setUserId(baseInfo.getUserId());
                              greetUserMessage.setNickName(baseInfo.getNickName());
                              greetUserMessage.setPortraitUrl(
                                  LoboUtil.getImageUrl(baseInfo.getPortrait()));
                              greetUserMessage.setYunXinId(baseInfo.getYunXinId());
                              greetUserMessage.setMessage(message);
                              greetUserMessage.setIsVip(isVip);

                              return greetUserMessage;
                            }));
              }
              return Mono.zip(
                  monos,
                  v ->
                      Arrays.stream(v)
                          .map(item -> (SendGreetUserMessageRes.GreetUserMessage) item)
                          .collect(Collectors.toList()));
            });
  }

  private Mono<List<String>> getGreetMessages(Long userId) {
    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(
            config -> {
              List<String> messages = new ArrayList<>();
              if (config.getCustomMessages() != null) {
                messages.addAll(
                    config.getCustomMessages().stream()
                        .map(UserGreetConfig.Message::getContent)
                        .collect(Collectors.toList()));
              }
              if (config.getSystemMessageIds() != null) {
                return systemGreetMessageRepository
                    .findAllById(config.getSystemMessageIds())
                    .map(message -> messages.add(message.getContent()))
                    .collectList()
                    .map(
                        v -> {
                          // 没有定义消息
                          if (messages.isEmpty()) {
                            throw ErrorCode.UNAVAILABLE_GREET_MESSAGE.newBusinessException();
                          }
                          return messages;
                        });
              }
              if (messages.isEmpty()) {
                throw ErrorCode.UNAVAILABLE_GREET_MESSAGE.newBusinessException();
              }
              return Mono.just(messages);
            })
        .switchIfEmpty(Mono.error(ErrorCode.UNAVAILABLE_GREET_MESSAGE.newBusinessException()));
  }

  private Mono<GreetInfo> validateIsGreetable(
      Long userId, DictionaryProperties.GreetMessage greetConfig) {
    return userManager
        .getUser(userId)
        .flatMap(
            user -> {
              if (!user.getIsEnableGreet()) {
                throw ErrorCode.NOT_OPENED_GREET.newBusinessException();
              }
              RBucketReactive<GreetInfo> greetInfoBucket =
                  singleRedission.getBucket(key(RedisConstant.USER_GREET_INFO, userId));
              return greetInfoBucket
                  .get()
                  .map(
                      info -> {
                        LocalDateTime minSendTime =
                            info.getLastGreetTime()
                                .plusSeconds(greetConfig.getSendIntervalSecond());
                        // 当前是否在可发送时间之后
                        if (LocalDateTime.now().isBefore(minSendTime)) {
                          throw ErrorCode.GREET_TIME_LIMIT.newBusinessException();
                        }
                        return info;
                      })
                  .defaultIfEmpty(new GreetInfo(0, LocalDateTime.now()));
            });
  }
}
