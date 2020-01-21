package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 登录类型 */
@AllArgsConstructor
@Getter
public enum LoginType implements Metadata {
  WECHAT(2, "微信登录"),
  PHONE_SMS_CODE(3, "手机号验证码登录"),
  PHONE_PASSWORD(4, "手机号密码登录");

  private int code;
  private String desc;
}
