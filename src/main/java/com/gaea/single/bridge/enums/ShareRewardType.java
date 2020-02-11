package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 广告类型 */
@Getter
@AllArgsConstructor
public enum ShareRewardType implements Metadata {
  REGISTER(1, "注册充值分销"),
  RECHARGE(2, "充值分销"),
  VIDEO(3, "视频收入分销"),
  VIDEO_GRATUITY(4, "视频打赏收入分销"),
  IM_GRATUITY(5, "IM打赏收入分销");

  private Integer code;
  private String desc;

  public static ShareRewardType ofCode(int code) {
    for (ShareRewardType type : ShareRewardType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
