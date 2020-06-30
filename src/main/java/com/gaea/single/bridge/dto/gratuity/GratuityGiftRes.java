package com.gaea.single.bridge.dto.gratuity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("打赏礼物信息")
@AllArgsConstructor
@NoArgsConstructor
public class GratuityGiftRes {
  @ApiModelProperty(value = "礼物id", required = true)
  private Long giftId;

  @ApiModelProperty(value = "礼物图标链接", required = true)
  private String giftIconUrl;

  @ApiModelProperty(value = "礼物数量", required = true)
  private Integer giftNum;
}
