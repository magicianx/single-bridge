package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 礼物类型 */
@AllArgsConstructor
@Getter
public enum GiftType implements Metadata {
  GENERAL(1, "常规礼物"),
  GRADE(1, "等级礼物"),
  SVIP(1, "svip礼物");

  private int code;
  private String desc;

  public static GiftType ofCode(int code) {
    for (GiftType type : GiftType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
