package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** vip 类型 */
@AllArgsConstructor
@Getter
public enum VipType implements Metadata {
  MONTH_VIP(1, "月度vip"),
  QUARTER_VIP(2, "季度vip"),
  ANNUAL_VIP(3, "年度vip"),
  PERMANENT_VIP(4, "永久vip"); // 目前single取消此vip类型

  private int code;
  private String desc;

  public static VipType ofCode(int code) {
    for (VipType type : VipType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
