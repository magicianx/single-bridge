package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 用户在线状态 */
@AllArgsConstructor
@Getter
public enum UserOnlineStatus implements Metadata {
  OFFLINE(0, "下线"),
  UN_DISTURB(1, "勿扰"),
  FREE(2, "空闲"),
  CHATTING(3, "忙碌"),
  ONLINE(4, "上线"),
  UNKNOWN(99, "未知");

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
