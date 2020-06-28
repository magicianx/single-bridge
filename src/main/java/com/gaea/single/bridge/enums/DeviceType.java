package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 设备类型 */
@Getter
@AllArgsConstructor
public enum DeviceType implements Metadata {
  ANDROID(1, "安卓"),
  IOS(2, "IOS");

  private int code;
  private String desc;
}
