package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 媒体呼叫类型 */
@Getter
@AllArgsConstructor
public enum MediaCallType implements Metadata {
  DIRECT(1, "正常呼叫"),
  INVITE(2, "邀请对方呼叫");

  private int code;
  private String desc;
}
