package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@ApiModel("绑定用户手机号请求")
@Getter
@Setter
public class BindUserPhoneReq {
  @ApiModelProperty(value = "手机号", required = true)
  @NotBlank
  private String userMobile;

  @ApiModelProperty(value = "短信验证码", required = true)
  @NotBlank
  private String smsCode;
}
