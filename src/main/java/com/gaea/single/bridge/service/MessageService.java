package com.gaea.single.bridge.service;

import reactor.core.publisher.Mono;

/** 运行消息服务 */
public interface MessageService {
  /**
   * 获取普通用户剩余发送消息次数, 默认为10
   *
   * @param userId 用户id
   * @return int 剩余发送消息数量
   */
  Mono<Integer> getMessageCount(Long userId);

  /** 将剩余发送消息数量减1 */
  Mono<Void> decrMessageCount(Long userId);
}
