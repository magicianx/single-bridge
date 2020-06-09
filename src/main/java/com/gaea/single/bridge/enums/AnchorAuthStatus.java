package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 主播认证状态 */
@Getter
@AllArgsConstructor
public enum AnchorAuthStatus implements Metadata {
  UNAUTH(1, "未认证"),
  AUTHED(2, "已认证"),
  AUTHING(3, "认证中"),
  PASS(4, "拉黑"),
  UNPASS(6, "认证未通过");

  private int code;
  private String desc;

  public static AnchorAuthStatus ofCode(int code) {
    for (AnchorAuthStatus status : AnchorAuthStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }

  /**
   * 是否认证通过
   *
   * @return boolean
   */
  public boolean isAuditPass() {
    return this == AnchorAuthStatus.AUTHED;
  }
}
