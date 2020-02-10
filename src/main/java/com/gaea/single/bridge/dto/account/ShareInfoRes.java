package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("分j销信息")
public class ShareInfoRes {
  @ApiModelProperty(value = "可提现金额(元)", required = true)
  private BigDecimal withdrawableAmount;

  @ApiModelProperty(value = "累计奖励金额(元)", required = true)
  private BigDecimal rewardAmount;

  @ApiModelProperty(value = "累计邀请", required = true)
  private Integer inviteCount;

  @ApiModelProperty(value = "是否可提现", required = true)
  private Boolean withdrawable;

  @ApiModelProperty(value = "充值提成百分比", required = true)
  private Float rechargePercentage;

  @ApiModelProperty(value = "收入提成百分比", required = true)
  private Float incomePercentage;

  @ApiModelProperty(value = "收益持续天数ø", required = true)
  private Integer profitDurationDays;
}
