package com.gaea.single.bridge.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  SUCCESS(200, "成功"),
  BAD_REQUEST(400, "无效请求: %s"),
  INNER_ERROR(9999, "系统内部错误"),
  DUPLICATE_REPORT(1001, "已有投诉正在处理，不可重复投诉"),
  USER_INFO_AUDITING(1002, "个人资料正在审核中"),
  INVALID_VIDEO_ORDER(1003, "订单无效"),
  AGE_LESS_THAN_LIMIT(1004, "本产品建议18岁以上用户使用"),
  CUSTOM_GREET_MESSAGE_LIMIT_COUNT(1005, "自定义打招呼数量达到上限"),
  UNAVAILABLE_GREET_MESSAGE(1006, "请先选择打招呼文案! "),
  GREET_TIME_LIMIT(1008, "已修改缓冲时间,请重新登录!"),
  NOT_OPENED_GREET(1009, "未开启打招呼功能"),
  UNAVAILABLE_GREET_USER(1010, "今天在线的用户已被全部发送完 请明天再来吧!"),
  INVALID_SESSION(10016, "session过期，请重新登录!");

  private int code;
  private String message;

  public BusinessException newBusinessException() {
    return new BusinessException(this.code, this.message);
  }

  public BusinessException newBusinessException(Object... formatParams) {
    return new BusinessException(this.code, String.format(this.message, formatParams));
  }

  public static boolean isSuccess(int code) {
    return code == ErrorCode.SUCCESS.getCode();
  }
}
