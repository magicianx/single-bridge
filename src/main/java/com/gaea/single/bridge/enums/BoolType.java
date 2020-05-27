package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author cludy */
@AllArgsConstructor
public enum BoolType {
  TRUE(1, true),
  FALSE(0, false);

  private long code;
  private boolean value;

  public static BoolType ofCode(long code) {
    for (BoolType v : BoolType.values()) {
      if (v.code == code) {
        return v;
      }
    }
    return null;
  }

  public static BoolType ofValue(boolean value) {
    for (BoolType v : BoolType.values()) {
      if (v.value == value) {
        return v;
      }
    }
    return null;
  }

  public long getCode() {
    return code;
  }

  public boolean getValue() {
    return value;
  }
}
