package com.gaea.single.bridge.service;

import reactor.core.publisher.Mono;

/**
 * 支付服务
 *
 * @author JunJYu
 * @date 2020/7/9 16:01
 */
public interface PayService {

  /**
   * 用户是否为第一次充值/开通VIP
   *
   * @param userId 用户id
   * @return Mono<Boolean>
   */
  Mono<Boolean> isFirstRecharge(Long userId);
}
