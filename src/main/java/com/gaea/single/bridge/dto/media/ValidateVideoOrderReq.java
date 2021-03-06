package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("验证视频订单请求")
public class ValidateVideoOrderReq {
  @ApiModelProperty(value = "订单临时id", required = true)
  @NotBlank
  private String orderTempId;
}
