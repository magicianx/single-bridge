package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 验证码类型 */
@Getter
@AllArgsConstructor
public enum SmsCodeType implements Metadata {
  SMS_CODE_LOGIN(2, "验证码登录"),
  UNBIND_ALIPAY(2, "解绑支付宝"),
  BIND_PHONE(3, "绑定手机号");

  private int code;
  private String desc;
}
