package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 支付场景 */
@Getter
@AllArgsConstructor
public enum PayScene implements Metadata {
  IN_VIDEO(1, "视频内充值"),
  OUT_VIDEO(2, "视频外充值 "),
  ACTIVE_VIP(4, "开通vip"),
  BUY_GIFT(6, "购买礼包");

  private Integer code;
  private String desc;
}
