package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "解绑支付宝账号请求")
public class UnbindAlipayReq {
  @ApiModelProperty(value = "手机号", required = true)
  @NotBlank
  private String phoneNum;

  @ApiModelProperty(value = "验证码", required = true)
  @NonNull
  private String smsCode;
}
