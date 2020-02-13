package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 分享方式类型 */
@Getter
@AllArgsConstructor
public enum ShareWayType implements Metadata {
  WECHAT_FRIEND(1, "微信朋友"),
  WECHAT_FRIEND_CIRCLE(2, "微信朋友圈");

  private Integer code;
  private String desc;
}
