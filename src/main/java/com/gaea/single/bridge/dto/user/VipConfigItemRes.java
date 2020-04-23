package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.VipType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@ApiModel("vip价格配置项")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VipConfigItemRes {
  @ApiModelProperty(value = "配置id", required = true)
  private Long id;

  @ApiModelProperty(value = "类型", required = true)
  private VipType type;

  @ApiModelProperty(value = "总价(元)", required = true)
  private BigDecimal price;

  @ApiModelProperty(value = "钻石价格", required = true)
  private Integer diamonds;

  @ApiModelProperty(value = "每日价格(元)", required = true)
  private BigDecimal dayPrice;
}
