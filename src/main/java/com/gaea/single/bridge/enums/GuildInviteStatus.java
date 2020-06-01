package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户加入公会状态
 *
 * @author cludy
 */
@Getter
@AllArgsConstructor
public enum GuildInviteStatus {
  JOINED(1, "已加入"),
  WAITING_CONFIRM(2, "待确认"),
  REFUSED(3, "拒绝加入");

  private int code;
  private String desc;

  public static GuildInviteStatus ofCode(int code) {
    for (GuildInviteStatus status : GuildInviteStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
