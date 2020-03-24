package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel(value = "提现请求")
public class WithdrawReq {
  @ApiModelProperty("提现金额(元)")
  @NotNull
  private BigDecimal money;
}
