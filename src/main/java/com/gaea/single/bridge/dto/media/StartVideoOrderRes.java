package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("开始视频订单响应")
@AllArgsConstructor
@NoArgsConstructor
public class StartVideoOrderRes {
  @ApiModelProperty(value = "订单id", required = true)
  private String orderId;

  @ApiModelProperty(value = "用户钻石余额(非主播)", required = true)
  private Long diamondBalance;
}
