package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("创建视频订单请求")
public class CreateVideoOrderReq {
  @ApiModelProperty(value = "被叫用户id", required = true)
  private Long calledId;
}
