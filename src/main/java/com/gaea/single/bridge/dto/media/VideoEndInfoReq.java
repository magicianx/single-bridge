package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("开始视频订单请求")
public class VideoEndInfoReq {
  @ApiModelProperty(value = "订单临时id", required = true)
  @NotBlank
  private String orderTempId;

  @ApiModelProperty(value = "订单id", required = true)
  @NotBlank
  private String orderId;

  @ApiModelProperty(value = "被呼叫用户id", required = true)
  @NotNull
  private Long calledUserId;
}
