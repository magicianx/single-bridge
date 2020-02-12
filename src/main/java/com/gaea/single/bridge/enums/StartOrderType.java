package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 视频订单类型 */
@Getter
@AllArgsConstructor
public enum StartOrderType implements Metadata {
  START(1, "开始订单"), // 由被叫方35秒后发送
  START_CHARGING(2, "5秒开始计费"); // 由主叫方5秒后发送

  private Integer code;
  private String desc;
}
