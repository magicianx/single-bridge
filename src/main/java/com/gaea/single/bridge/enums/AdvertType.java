package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 广告类型
 *
 * @author cludy
 */
@Getter
@AllArgsConstructor
public enum AdvertType implements Metadata {
  INNER(2, "应用内打开"),
  OUTER(1, "应用外打开");

  private int code;
  private String desc;

  public static AdvertType ofCode(int code) {
    for (AdvertType type : AdvertType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
