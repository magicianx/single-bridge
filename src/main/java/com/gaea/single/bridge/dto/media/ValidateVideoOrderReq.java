package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("验证视频订单请求")
public class ValidateVideoOrderReq {
  @ApiModelProperty(value = "订单id", required = true)
  private String orderId;
}
