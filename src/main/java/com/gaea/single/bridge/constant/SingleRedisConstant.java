package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * redis缓存key定义
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SingleRedisConstant {
  /**
   * 用户消息数量 + userId <br>
   * type: text
   */
  public static final String USER_MESSAGE_COUNT = "user:message_count:";

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
}
