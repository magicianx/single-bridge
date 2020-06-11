package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/** @author cludy */
@ApiModel("用户备注信息")
@Getter
@Setter
public class UserRemarkRes {
  @ApiModelProperty(value = "被备注的用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "备注", required = true)
  private String remark;
}
