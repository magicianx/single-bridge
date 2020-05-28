package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.converter.UserGreetConverter;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import com.gaea.single.bridge.entity.mongodb.SystemGreetMessage;
import com.gaea.single.bridge.entity.mongodb.User;
import com.gaea.single.bridge.entity.mongodb.UserGreetConfig;
import com.gaea.single.bridge.repository.mongodb.SystemGreetMessageRepository;
import com.gaea.single.bridge.repository.mongodb.UserGreetConfigRepository;
import com.gaea.single.bridge.repository.mongodb.UserRepository;
import com.gaea.single.bridge.service.UserGreetService;
import com.gaea.single.bridge.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** @author cludy */
@Service
public class UserGreetServiceImpl implements UserGreetService {
  @Autowired private UserRepository userRepository;
  @Autowired private UserGreetConfigRepository userGreetConfigRepository;
  @Autowired private SystemGreetMessageRepository systemGreetMessageRepository;

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
                              == DictionaryProperties.get().getGreetMessage().getMaxCount()) {
                            return Mono.error(
                                ErrorCode.CUSTOM_GREET_MESSAGE_LIMIT_COUNT.newBusinessException());
                          }
                          UserGreetConfig.Message message =
                              new UserGreetConfig.Message(StringUtil.uuid(), content);
                          config.getCustomMessages().add(message);
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
  public Mono<Boolean> isEnableGreet(Long userId) {
    return userRepository.findById(userId).map(User::getIsEnableGreet);
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
}
