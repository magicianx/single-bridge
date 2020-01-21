package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("登录响应")
public class LoginRes {
  @ApiModelProperty(value = "用户id", required = true)
  private String userId;

  @ApiModelProperty(value = "session", required = true)
  private String session;
}
