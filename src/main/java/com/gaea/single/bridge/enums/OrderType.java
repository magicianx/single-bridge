package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 订单类型 */
@Getter
@AllArgsConstructor
public enum OrderType implements Metadata {
  VIDEO_PAY(1, "视频支出"),
  VIDEO_INCOME(2, "视频收入"),
  GRATUITY_PAY(3, "礼物支出"),
  GRATUITY_INCOME(4, "礼物收入"),
  IM_PAY(5, "消息支出"),
  IM_INCOME(6, "消息收入"),
  RECHARGE(7, "充值"),
  WITHDRAW(8, "提现"),
  RECHARGE_GIVE(9, "充值赠送"),
  REFUND(10, "退款"),
  NEW_RED_PACKET(11, "新手红包"),
  SYSTEM_REISSUE_ORDER(12, "系统补单"),
  PLATFORM_ACTIVITY_PAY(13, "平台活动支出"),
  PLATFORM_ACTIVITY_INCOME(14, "平台活动收入");

  private Integer code;
  private String desc;

  public static OrderType ofCode(int code) {
    for (OrderType status : OrderType.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
