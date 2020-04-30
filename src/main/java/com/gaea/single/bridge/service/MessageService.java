package com.gaea.single.bridge.service;

import com.gaea.single.bridge.enums.OsType;
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

  /**
   * 将剩余发送消息数量减1
   *
   * @param userId 用户id
   * @return 剩余消息数量
   */
  Mono<Integer> decrMessageCount(Long userId);

  /**
   * 批量发送文本消息
   *
   * @param osType {@link OsType}
   * @param content 消息内容
   */
  Mono<Void> batchSendTextMsg(OsType osType, String content);
}
