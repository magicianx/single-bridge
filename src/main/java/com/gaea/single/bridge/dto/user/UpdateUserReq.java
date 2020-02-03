package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@ApiModel("编辑用户信息请求")
@Getter
@Setter
public class UpdateUserReq {
  @ApiModelProperty(
      value = "参数名称",
      allowableValues = "nickName,intro,gender,birthday",
      required = true)
  @NotBlank
  String name;

  @ApiModelProperty(value = "参数值", required = true)
  @NotBlank
  String value;
}
