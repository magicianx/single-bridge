package com.gaea.single.bridge.constant;

import com.gaea.single.bridge.error.ErrorCode;

public interface ErrorCodes {
  ErrorCode SUCCESS = ErrorCode.of(200, "成功");
  ErrorCode INNER_ERROR = ErrorCode.of(9999, "系统内部错误");
}
