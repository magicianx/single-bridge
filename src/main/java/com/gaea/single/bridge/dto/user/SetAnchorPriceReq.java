package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("设置主播单价请求")
@Getter
@Setter
public class SetAnchorPriceReq {
  @ApiModelProperty(value = "单价", required = true)
  @NotNull
  private Integer videoPrice;

  @ApiModelProperty(value = "单价对应等级", required = true)
  @NotNull
  private Integer videoGrade;
}
