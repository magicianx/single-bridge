package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("标签信息")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LabelRes {
  @ApiModelProperty("标签id")
  private Long id;

  @ApiModelProperty("名称")
  private String name;

  @ApiModelProperty("颜色")
  private String color;
}
