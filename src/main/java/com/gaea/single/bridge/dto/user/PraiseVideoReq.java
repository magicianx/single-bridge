package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("点赞视频请求")
@Getter
@Setter
public class PraiseVideoReq {
  @ApiModelProperty(value = "视频id", required = true)
  @NotNull
  private Long id;
}
