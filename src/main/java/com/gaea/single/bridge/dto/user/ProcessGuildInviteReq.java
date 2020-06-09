package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/** @author cludy */
@ApiModel("处理公会邀请请求")
@Getter
@Setter
public class ProcessGuildInviteReq {
  @ApiModelProperty(value = "是否同意加入", required = true)
  @NotNull
  private Boolean isAgree;
}
