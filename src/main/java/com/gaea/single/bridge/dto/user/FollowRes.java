package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.FollowStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("关注响应")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowRes {
  @ApiModelProperty(value = "关注状态", required = true)
  private FollowStatus followStatus;
}
