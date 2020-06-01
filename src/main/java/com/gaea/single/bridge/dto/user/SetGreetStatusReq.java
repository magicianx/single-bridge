package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/** @author cludy */
@Getter
@Setter
@Api("设置打招呼功能状态请求")
public class SetGreetStatusReq {
  @ApiModelProperty(value = "是否开启打招呼", required = true)
  @NotNull
  private Boolean isEnable;
}
