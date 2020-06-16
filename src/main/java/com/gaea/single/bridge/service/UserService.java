package com.gaea.single.bridge.service;

import reactor.core.publisher.Mono;

/** @author cludy */
public interface UserService {
  /**
   * 是否开启定位
   *
   * @return {@link Mono<Boolean>}
   */
  Mono<Boolean> isEnablePosition(Long userId);

  /**
   * 切换定位状态
   *
   * @param userId 用户id
   * @param isEnable 是否开启
   * @return {@link Mono<Boolean>}
   */
  Mono<Void> switchPositionStatus(Long userId, boolean isEnable);

  /**
   * 是否为公会用户
   *
   * @param userId 用户id
   * @return {@link Mono<Boolean>}
   */
  Mono<Boolean> isGuildUser(Long userId);
}
