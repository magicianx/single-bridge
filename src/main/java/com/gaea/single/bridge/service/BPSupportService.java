package com.gaea.single.bridge.service;

import reactor.core.publisher.Mono;

public interface BPSupportService {
  /**
   * 是否为第一次充值
   *
   * @param userId 用户id
   * @return Mono<Boolean>
   */
  Mono<Boolean> isFirstRecharge(Long userId);

  /**
   * 是否为当日注册并且首次充值
   *
   * @param userId 用户id
   * @return Mono<Boolean>
   */
  Mono<Boolean> isTodayRegisterAndFirstRecharge(Long userId);

  /**
   * 是否为首次开通vip
   *
   * @param userId 用户id
   * @return Mono<Boolean>
   */
  Mono<Boolean> isFirstOpenVip(Long userId);
}
