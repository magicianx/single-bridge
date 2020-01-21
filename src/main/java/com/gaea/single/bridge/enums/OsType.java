package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OsType implements Metadata {
  android(1, "安卓"),
  iso(2, "ios");

  private int code;
  private String desc;
}
