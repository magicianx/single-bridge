package com.gaea.single.bridge.error;

import com.gaea.single.bridge.core.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  SUCCESS(200, "成功"),
  BAD_REQUEST(400, "无效请求"),
  INNER_ERROR(9999, "系统内部错误"),
  DUPLICATE_REPORT(1001, "已有投诉正在处理，不可重复投诉"),
  USER_INFO_AUDITING(1002, "个人资料正在审核中"),
  INVALID_VIDEO_ORDER(1003, "订单无效");

  private int code;
  private String message;

  public BusinessException newBusinessException() {
    return new BusinessException(this.code, this.message);
  }
}
