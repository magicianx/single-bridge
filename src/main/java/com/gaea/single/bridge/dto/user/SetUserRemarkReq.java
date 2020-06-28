package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("设置用户备注请求")
@Getter
@Setter
public class SetUserRemarkReq {
  @ApiModelProperty(value = "被备注用户id", required = true)
  @NotNull
  private Long userId;

  @ApiModelProperty(value = "备注", required = true)
  private String remark;
}
