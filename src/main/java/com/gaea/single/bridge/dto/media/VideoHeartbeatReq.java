package com.gaea.single.bridge.dto.media;

import com.gaea.single.bridge.enums.StartOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("视频心跳请求")
public class VideoHeartbeatReq {
  @ApiModelProperty(value = "视频订单id", required = true)
  @NotBlank
  private String orderId;

  @ApiModelProperty(value = "心跳请求类型", required = true)
  @NotNull
  private StartOrderType type;
}
