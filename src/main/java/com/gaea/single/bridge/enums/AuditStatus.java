package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 审核状态 */
@Getter
@AllArgsConstructor
public enum AuditStatus implements Metadata {
  AUDITING(1, "审核中"),
  PASS(2, "审核通过"),
  NO_PASS(3, "审核未通过"),
  PULL_BLACK(4, "拉黑中");

  private Integer code;
  private String desc;

  public static AuditStatus ofCode(int code) {
    for (AuditStatus status : AuditStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
