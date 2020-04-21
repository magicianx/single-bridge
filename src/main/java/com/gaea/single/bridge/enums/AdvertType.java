package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 广告类型 */
@Getter
@AllArgsConstructor
public enum AdvertType implements Metadata {
  INNER("应用内打开"),
  OUTER("应用外打开");

  private String desc;
}
