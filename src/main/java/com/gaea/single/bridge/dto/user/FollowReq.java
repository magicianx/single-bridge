package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("关注请求")
@Getter
@Setter
public class FollowReq {
  @ApiModelProperty(value = "用户id", required = true)
  @NotNull
  private Long userId;
}
