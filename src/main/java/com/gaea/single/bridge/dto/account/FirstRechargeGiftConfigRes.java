package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("首充礼包配置信息")
public class FirstRechargeGiftConfigRes {

  @ApiModelProperty(value = "首充说明", required = true)
  private String desc;

  @ApiModelProperty(value = "充值礼包列表", required = true)
  private List<RechargeGift> gifts;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("礼包信息")
  public static class RechargeGift {
    @ApiModelProperty(value = "礼物id", required = true)
    private Long giftId;

    @ApiModelProperty(value = "礼包价格(元)", required = true)
    private BigDecimal money;

    @ApiModelProperty(value = "礼包中的钻石数量", required = true)
    private Integer giftDiamonds;

    @ApiModelProperty(value = "礼包中的vip天数", required = true)
    private Integer giftVipDays;
  }
}
