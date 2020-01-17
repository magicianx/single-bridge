package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 主播在线状态 */
@AllArgsConstructor
@Getter
public enum AnchorOnlineStatus implements Metadata {
  FREE(1, "空闲"),
  CHATTING(2, "空闲"),
  UN_DISTURB(3, "勿扰");

  private int code;
  private String desc;

  public static AnchorOnlineStatus ofCode(int code) {
    for (AnchorOnlineStatus status : AnchorOnlineStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
