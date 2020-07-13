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
  PLATFORM_ACTIVITY_INCOME(14, "平台活动收入"),
  MASONRY_OUT(15, "钻石兑换"),
  MASONRY_INCOME(16, "钻石兑换"),
  AUDIO_OUT(17, "语音支出"),
  AUDIO_INCOME(18, "语音收入"),
  AUDIO_GRATUITY_PAY(19, "语音礼物支出"),
  AUDIO_GRATUITY_INCOME(20, "语音礼物收入"),
  PRIVILEGE_GIVE(21, "购买特殊身份赠送"),
  DYNAMIC_INCOME(22, "动态广场打赏收入"),
  DYNAMIC_PAY(23, "动态广场打赏支出"),
  VOD_INCOME(24, "小视频打赏收入"),
  VOD_PAY(25, "小视频打赏支出"),
  BUY_FIRST_RECHARGE_GIFT(26, "购买礼包");

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
