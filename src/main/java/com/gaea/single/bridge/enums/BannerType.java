package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 广告类型 */
@Getter
@AllArgsConstructor
public enum BannerType implements Metadata {
  INNER("应用内打开");

  private String desc;
}
