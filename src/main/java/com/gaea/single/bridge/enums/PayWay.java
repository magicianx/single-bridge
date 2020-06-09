package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 支付方式 */
@Getter
@AllArgsConstructor
public enum PayWay implements Metadata {
  ALIPAY("支付宝支付"),
  WECHAT_PAY("微信支付"),
  APPLE_PAY("苹果内购"),
  LEGEND_SHOP_PAY("小羊商城支付");

  private String desc;
}
