package com.gaea.single.bridge.error;

import com.gaea.single.bridge.support.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorCode {
  public final int code;
  public final String massage;

  public static ErrorCode of(int code, String message) {
    return new ErrorCode(code, message);
  }

  public void throwBusinessException() {
    throw newBusinessException();
  }

  public BusinessException newBusinessException() {
    return new BusinessException(this);
  }
}
