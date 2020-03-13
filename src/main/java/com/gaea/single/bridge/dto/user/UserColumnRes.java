package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("栏目信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserColumnRes {
  @ApiModelProperty(value = "栏目id", required = true)
  private Long id;

  @ApiModelProperty(value = "栏目名称", required = true)
  private String name;
}
