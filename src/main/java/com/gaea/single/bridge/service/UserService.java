package com.gaea.single.bridge.service;

import reactor.core.publisher.Mono;

public interface UserService {
  /**
   * 当前登录用户渠道是否和注册渠道是同一个渠道
   *
   * @param userId 用户id
   * @param channel 渠道号
   * @return Mono<Boolean>
   */
  Mono<Boolean> isInRegisterChannel(Long userId, String channel);
}
