package com.gaea.single.bridge.core.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private int code;

  public BusinessException(int code, String message) {
    super(message);
    this.code = code;
  }
}
