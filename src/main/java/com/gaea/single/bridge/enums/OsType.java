package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OsType implements Metadata {
  ANDROID(1, "安卓"),
  IOS(2, "ios");

  private int code;
  private String desc;
}
