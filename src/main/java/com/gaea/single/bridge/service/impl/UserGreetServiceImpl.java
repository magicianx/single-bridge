package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.RedisConstant;
import com.gaea.single.bridge.converter.UserGreetConverter;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.manager.GreetUserManager;
import com.gaea.single.bridge.core.manager.model.GreetInfo;
import com.gaea.single.bridge.core.manager.model.GreetUser;
import com.gaea.single.bridge.core.yx.YxClient;
import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.GreetStatusRes;
import com.gaea.single.bridge.dto.user.SendGreetMessageRes;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import com.gaea.single.bridge.entity.mongodb.SystemGreetMessage;
import com.gaea.single.bridge.entity.mongodb.UserGreetConfig;
import com.gaea.single.bridge.repository.mongodb.SystemGreetMessageRepository;
import com.gaea.single.bridge.repository.mongodb.UserGreetConfigRepository;
import com.gaea.single.bridge.repository.mongodb.UserRepository;
import com.gaea.single.bridge.repository.mysql.UserRegInfoRepository;
import com.gaea.single.bridge.service.UserGreetService;
import com.gaea.single.bridge.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** @author cludy */
@Service
public class UserGreetServiceImpl implements UserGreetService {
  @Autowired private UserRepository userRepository;
  @Autowired private UserGreetConfigRepository userGreetConfigRepository;
  @Autowired private SystemGreetMessageRepository systemGreetMessageRepository;
  @Autowired private UserRegInfoRepository userRegInfoRepository;
  @Autowired private GreetUserManager greetUserManager;
  @Autowired private YxClient yxClient;
  @Autowired private RedissonReactiveClient redission;

  @Override
  public Mono<UserGreetConfigRes> getGreetConfig(Long userId) {
    Mono<List<SystemGreetMessage>> allMessageMono =
        systemGreetMessageRepository.findAll().collectList();

    return userGreetConfigRepository
        .findByUserId(userId)
        .flatMap(config -> allMessageMono.map(messages -> Pair.of(config, messages)))
        .switchIfEmpty(
            Mono.defer(
                () ->
                    // 不存在则创建
                    allMessageMono.flatMap(
                        systemMessages -> {
                          List<String> systemIds =
                              systemMessages.stream()
                                  .map(SystemGreetMessage::getId)
                                  .collect(Collectors.toList());

                          UserGreetConfig config = new UserGreetConfig();
                          config.setUserId(userId);
                          config.setCustomMessages(Collections.emptyList());
                          config.setSystemMessageIds(systemIds);
                          return userGreetConfigRepository
                              .save(config)
                              .map(saved -> Pair.of(saved, systemMessages));
                        })))
        .map(UserGreetConverter.toUserGreetConfigRes::convert);
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
    return userRepository
        .findById(userId)
        .map(
            user ->
                new GreetStatusRes(
                    user.getIsEnableGreet(),
                    DictionaryProperties.get().getGreetMessage().getSendIntervalSecond()));
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
  public Mono<SendGreetMessageRes> sendGreetMessage(Long userId) {
    DictionaryProperties.GreetMessage greetConfig = DictionaryProperties.get().getGreetMessage();
    return userRegInfoRepository
        .findById(userId)
        .flatMap(
            currentUser ->
                validateIsGreetable(userId, greetConfig)
                    .flatMap(
                        info ->
                            getGreetMessages(userId)
                                .flatMap(
                                    messages ->
                                        greetUserManager
                                            .getGreetUsers(greetConfig.getOneMaxSendGreetCount())
                                            .flatMap(
                                                users ->
                                                    sendGreetMessage(
                                                            currentUser.getYunxinId(),
                                                            users,
                                                            messages)
                                                        .then(
                                                            redission
                                                                .getBucket(
                                                                    RedisConstant.USER_GREET_INFO)
                                                                .set(
                                                                    new GreetInfo(
                                                                        info.getGreetTimes() + 1,
                                                                        LocalDateTime.now()))
                                                                .thenReturn(
                                                                    new SendGreetMessageRes(
                                                                        greetConfig
                                                                            .getSendIntervalSecond())))))));
  }

  @Override
  public Mono<Void> setGreetStatus(Long userId, boolean isEnable) {
    return userRepository
        .findById(userId)
        .flatMap(
            user -> {
              user.setIsEnablePosition(isEnable);
              return userRepository.save(user).then();
            });
  }

  private Mono<Void> sendGreetMessage(
      String yunXinId, Collection<GreetUser> users, List<String> messages) {
    int messageIndex = 0;
    List<Mono<Void>> monos = new ArrayList<>();
    for (GreetUser greetUser : users) {
      if (messageIndex == messages.size()) {
        messageIndex = 0;
      }
      messageIndex++;
      String message = messages.get(messageIndex);
      monos.add(
          yxClient.sendBatchTextMsg(
              yunXinId, Collections.singletonList(greetUser.getYunxinId()), message));
    }
    return Mono.zip(monos, (v) -> Mono.empty()).then();
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
    return userRepository
        .findById(userId)
        .flatMap(
            user -> {
              if (!user.getIsEnableGreet()) {
                throw ErrorCode.NOT_OPENED_GREET.newBusinessException();
              }

              RBucketReactive<GreetInfo> greetInfoBucket =
                  redission.getBucket(RedisConstant.USER_GREET_INFO);
              return greetInfoBucket
                  .get()
                  .map(
                      info -> {
                        LocalDateTime minSendTime =
                            info.getLastGreetTime()
                                .plusSeconds(greetConfig.getSendIntervalSecond());
                        // 当前是否在可发送时间之后
                        if (LocalDateTime.now().isAfter(minSendTime)) {
                          throw ErrorCode.GREET_TIME_LIMIT.newBusinessException();
                        }
                        return info;
                      })
                  .defaultIfEmpty(new GreetInfo(0, LocalDateTime.now()));
            });
  }
}
