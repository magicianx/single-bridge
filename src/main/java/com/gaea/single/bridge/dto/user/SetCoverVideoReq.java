package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("设置封面视频请求")
@Getter
@Setter
public class SetCoverVideoReq {
  @ApiModelProperty(value = "是否为封面", required = true)
  @NotNull
  private Boolean isCover;

  @ApiModelProperty(value = "视频id", required = true)
  @NotNull
  private Integer videoId;
}
