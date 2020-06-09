package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/** @author cludy */
@Getter
@Setter
@ApiModel("切换定位状态请求")
public class SwitchPositionStatusReq {
  @ApiModelProperty("是否启用定位")
  @NotNull
  private Boolean isEnable;
}
