package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * redis缓存key定义
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisConstant {
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
   * 用户消息数量 + userId <br>
   * type: text
   */
  public static final String USER_MESSAGE_COUNT = "user:message_count:";

  /**
   * 用户登录信息 + userId <br>
   * type: Map
   */
  public static final String USER_LOGIN_INFO = "user:login:";

  /**
   * 可以发送打招呼消息的新注册用户列表 <br>
   * type: zset <br>
   * value: Long
   */
  public static final String USER_GREET_NEW = "user:greet_new";

  /**
   * 可以发送打招呼消息的打开app后5分钟内未通话用户列表 <br>
   * type: zset <br>
   * value: Long
   */
  public static final String USER_GREET_UNCALLED = "user:greet_uncalled";

  /**
   * 正在使用的可以发送打招呼消息的用户列表 <br>
   * type: zset <br>
   * value: Long
   */
  public static final String USER_GREET_USING = "user:greet_using";

  /**
   * 用户进入已接收打招呼次数<br>
   * type: queue <br>
   * value: {@link Integer}
   */
  public static final String USER_GREET_TIMES = "user:greet_receive_times:";

  /**
   * 用户打招呼信息<br>
   * type: {@link com.gaea.single.bridge.core.manager.model.GreetInfo} <br>
   */
  public static final String USER_GREET_INFO = "user:greet_info:";

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
}
