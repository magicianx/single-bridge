package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@ApiModel("收入信息")
@Getter
@Setter
public class IncomeRes {
  @ApiModelProperty(value = "收入金额(元)）", required = true)
  private BigDecimal incomeAmount;

  @ApiModelProperty(value = "输入钻石", required = true)
  private Long diamonds;

  @ApiModelProperty(value = "支付账号", required = true)
  private String alipayAccount;

  @ApiModelProperty(value = "是否可提现", required = true)
  private Boolean withdrawable;

  @ApiModelProperty(value = "是否绑定支付宝账号", required = true)
  private Boolean isBindAlipay;

  @ApiModelProperty(value = "账号是否冻结", required = true)
  private Boolean isFrozen;

  @ApiModelProperty(value = "提现最低金额(元)", required = true)
  private BigDecimal withdrawLeastAmount;

  @ApiModelProperty(value = "提现日期(1..7对应星期一...星期日)", required = true)
  private Integer withdrawDay;
}
