package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 支付方式 */
@Getter
@AllArgsConstructor
public enum PayWay implements Metadata {
  ALIPAY(1, "支付宝支付"),
  WECHAT_PAY(1, "微信支付"),
  APPLE_PAY(2, "苹果内购");

  private Integer code;
  private String desc;
}
