package com.gaea.single.bridge.dto;

import com.gaea.single.bridge.support.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Result<T> {
  private Integer code;
  private T data;
  private String message;

  public static <T> Result<T> of(Integer code, T data, String message) {
    return new Result<>(code, data, message);
  }

  public static <T> Result<T> of(ErrorCode errorCode) {
    return new Result<>(errorCode.getCode(), null, errorCode.getMassage());
  }
}
