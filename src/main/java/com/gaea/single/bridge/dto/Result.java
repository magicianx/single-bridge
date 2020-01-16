package com.gaea.single.bridge.dto;

import com.gaea.single.bridge.constant.ErrorCodes;
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
  @ApiModelProperty("响应编码")
  private Integer code;

  @ApiModelProperty("响应业务数据")
  private T data;

  @ApiModelProperty("响应信息")
  private String message;

  public static <T> Result<T> success(T data) {
    return new Result<>(ErrorCodes.SUCCESS.getCode(), data, ErrorCodes.SUCCESS.getMassage());
  }

  public static <T> Result<T> error(ErrorCode errorCode) {
    return new Result<>(errorCode.getCode(), null, errorCode.getMassage());
  }
}
