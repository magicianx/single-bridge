package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 性别类型 */
@Getter
@AllArgsConstructor
public enum GenderType implements Metadata {
  MALE(1, "男性"),
  FEMALE(2, "女性");

  private int code;
  private String desc;

  public static GenderType ofCode(int code) {
    for (GenderType type : GenderType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
