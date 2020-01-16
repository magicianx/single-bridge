package com.gaea.single.bridge.support;

import com.gaea.single.bridge.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
