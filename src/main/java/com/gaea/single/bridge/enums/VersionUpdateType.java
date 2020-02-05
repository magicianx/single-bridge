package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 版本更新类型 */
@Getter
@AllArgsConstructor
public enum VersionUpdateType implements Metadata {
  CHOICE(1, "选择更新"),
  FORCE(2, "强制更新");

  private Integer code;
  private String desc;

  public static VersionUpdateType ofCode(int code) {
    for (VersionUpdateType type : VersionUpdateType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
