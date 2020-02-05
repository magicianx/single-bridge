package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 用户类型 */
@AllArgsConstructor
@Getter
public enum UserType implements Metadata {
  ANCHOR(1, "主播"),
  GENERAL_USER(0, "普通用户");

  private Integer code;
  private String desc;

  public static UserType ofCode(int code) {
    for (UserType type : UserType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
