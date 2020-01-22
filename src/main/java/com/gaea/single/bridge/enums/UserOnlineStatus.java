package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 用户在线状态 */
@AllArgsConstructor
@Getter
public enum UserOnlineStatus implements Metadata {
  FREE(1, "空闲"),
  CHATTING(2, "忙碌"),
  UN_DISTURB(3, "勿扰");

  private int code;
  private String desc;

  public static UserOnlineStatus ofCode(int code) {
    for (UserOnlineStatus status : UserOnlineStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
