package com.gaea.single.bridge.service;

import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.GreetStatusRes;
import com.gaea.single.bridge.dto.user.SendGreetMessageRes;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import reactor.core.publisher.Mono;

/**
 * 用户打招呼服务
 *
 * @author cludy
 */
public interface UserGreetService {
  Mono<UserGreetConfigRes> getGreetConfig(Long userId);

  Mono<GreetMessageRes> addCustomGreetMessage(Long userId, String content);

  Mono<Void> deleteCustomMessage(Long userId, String messageId);

  Mono<GreetStatusRes> getGreetStatus(Long userId);

  Mono<Void> useSystemGreetMessage(Long userId, String messageId, boolean isUse);

  Mono<SendGreetMessageRes> sendGreetMessage(Long userId);

  Mono<Void> setGreetStatus(Long userId, boolean isEnable);

  Mono<Void> addGreetUser(Long userId, boolean isNew);

  Mono<Void> removeGreetUser(Long userId);
}
