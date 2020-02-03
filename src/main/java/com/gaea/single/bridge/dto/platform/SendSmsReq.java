package com.gaea.single.bridge.dto.platform;

import com.gaea.single.bridge.enums.SmsCodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("发送验证码请求")
public class SendSmsReq {
  @ApiModelProperty(value = "验证码类型", required = true)
  @NotNull
  SmsCodeType type;

  @ApiModelProperty(value = "手机号", required = true)
  @NotBlank
  String phoneNum;
}
