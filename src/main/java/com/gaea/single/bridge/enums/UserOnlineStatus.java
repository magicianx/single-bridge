package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户在线状态
 *
 * @author cludy
 */
@AllArgsConstructor
@Getter
public enum UserOnlineStatus implements Metadata {
  UN_DISTURB(1, "勿扰"),
  FREE(2, "空闲"),
  CHATTING(3, "忙碌");

  private int code;
  private String desc;

  public static UserOnlineStatus ofCode(int code) {
    UserOnlineStatus onlineStatus = UserOnlineStatus.UN_DISTURB;
    for (UserOnlineStatus status : UserOnlineStatus.values()) {
      if (status.code == code) {
        onlineStatus = status;
        break;
      }
    }
    return onlineStatus;
  }
}
