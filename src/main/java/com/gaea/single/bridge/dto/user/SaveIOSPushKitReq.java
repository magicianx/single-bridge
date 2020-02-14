package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@ApiModel("保存IOS PushKit请求")
@Getter
@Setter
public class SaveIOSPushKitReq {
  @ApiModelProperty(value = "token", required = true)
  @NotBlank
  private String token;
}
