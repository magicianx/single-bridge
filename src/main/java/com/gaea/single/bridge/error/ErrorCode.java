package com.gaea.single.bridge.error;

import com.gaea.single.bridge.core.BusinessException;
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
  AGE_LESS_THAN_LIMIT(1004, "本产品建议18岁以上用户使用");

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
