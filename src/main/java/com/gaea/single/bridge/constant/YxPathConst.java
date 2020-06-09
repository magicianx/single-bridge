package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 云信消息路径
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YxPathConst {
  /** 批量发送消息 */
  public static final String BATCH_SEND_MESSAGE = "/nimserver/msg/sendBatchMsg.action";
}
