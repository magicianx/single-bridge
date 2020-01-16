package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("主播栏目信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnchorColumnRes {
  @ApiModelProperty(value = "id", required = true)
  private String id;

  @ApiModelProperty(value = "栏目名称", required = true)
  private String name;
}
