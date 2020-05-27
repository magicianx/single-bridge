package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * redis缓存key定义
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstant {
  /** 用户开启定位状态 */
  public static final String USER_POSITION_STATUS = "user:position_status:";

  /** 用户消息数量 */
  public static final String USER_MESSAGE_COUNT = "user:message_count:";

  /** 用户登录 */
  public static final String USER_LOGIN_INFO = "user:login:";
}
