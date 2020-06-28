package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * redis缓存key定义
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoboRedisConstant {
  /**
   * 用户信息 + userId<br>
   * type: Map
   */
  public static final String USER_INFO = "user:info:";

  /**
   * 用户开启定位状态 + userId<br>
   * type: Integer
   */
  public static final String USER_POSITION_STATUS = "user:position_status:";

  /**
   * 用户登录信息 + userId <br>
   * type: Map
   */
  public static final String USER_LOGIN_INFO = "user:login:";

  /**
   * 勿扰用户列表 <br>
   * type: list <br>
   * value: String
   */
  public static final String USER_UN_DISTURB = "user:dnd";

  /**
   * 空闲用户列表 <br>
   * type: list <br>
   * value: String
   */
  public static final String USER_FREE = "user:available";

  /**
   * 忙碌用户列表 <br>
   * type: list <br>
   * value: String
   */
  public static final String USER_BUSY = "user:busy";

  /**
   * 用户vip信息 <br>
   * type: Map
   */
  public static final String USER_VIP_INFO = "user:super:vip:";

  /**
   * 用户的真实在线状态 <br>
   * type: String
   */
  public static final String USER_REAL_STATUS = "user:real_status:";
}
