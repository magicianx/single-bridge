package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("主播单价列表")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnchorPriceItemRes {
  @ApiModelProperty(value = "价格项id", required = true)
  private Long id;

  @ApiModelProperty(value = "单价", required = true)
  private Integer price;

  @ApiModelProperty(value = "等级", required = true)
  private Integer grade;

  @ApiModelProperty(value = "是否可选择", required = true)
  private Boolean checkable;
}
