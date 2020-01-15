package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 设备类型 */
@Getter
@AllArgsConstructor
public enum DeviceType implements Metadata {
  IOS(1, "安卓"),
  ANDROID(2, "IOS");

  private int code;
  private String desc;
}
