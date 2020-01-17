package com.gaea.single.bridge.dto;

import com.gaea.single.bridge.error.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("全局响应")
public class Result<T> {
  @ApiModelProperty(value = "响应编码", example = "200")
  private int code;

  @ApiModelProperty("响应业务数据")
  private T data;

  @ApiModelProperty(value = "响应信息", example = "成功")
  private String message;

  public static <T> Result<T> success(T data) {

    return new Result<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
  }

  public static <T> Result<T> error(ErrorCode errorCode) {
    return new Result<>(errorCode.getCode(), null, errorCode.getMessage());
  }

  public static <T> Result<T> error(int code, String message) {
    return new Result<>(code, null, message);
  }
}
