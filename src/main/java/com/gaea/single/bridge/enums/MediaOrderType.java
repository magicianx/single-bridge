package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 媒体订单类型 */
@Getter
@AllArgsConstructor
public enum MediaOrderType implements Metadata {
  VIDEO(1, "视频订单"),
  VOICE(2, "语音订单");

  private int code;
  private String desc;

  public static MediaOrderType ofCode(int code) {
    for (MediaOrderType type : MediaOrderType.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}
