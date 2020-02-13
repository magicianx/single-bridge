package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "手动绑定支付宝账号请求")
public class ManualBindAlipayReq {
  @ApiModelProperty(value = "支付宝账号", required = true)
  @NonNull
  private String alipayAccount;
}
