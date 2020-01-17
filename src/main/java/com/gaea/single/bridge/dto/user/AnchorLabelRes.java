package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("主播标签信息")
public class AnchorLabelRes {
  @ApiModelProperty(value = "标签id", required = true)
  private Long id;

  @ApiModelProperty(value = "标签名称", required = true)
  private String name;

  @ApiModelProperty(value = "标签颜色", required = true)
  private String color;
}
