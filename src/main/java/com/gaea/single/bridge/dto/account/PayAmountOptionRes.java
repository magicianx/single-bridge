package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("支付金额选项")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayAmountOptionRes {
  @ApiModelProperty(value = "选项id", required = true)
  private Long id;

  @ApiModelProperty(value = "充值钻石数量", required = true)
  private Integer diamonds;

  @ApiModelProperty(value = "花费金额(元)", required = true)
  private Integer money;
}
