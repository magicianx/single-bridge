package com.gaea.single.bridge.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("应用信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoRes {
  @ApiModelProperty(value = "是否审核通过, 默认值为true", required = true)
  private Boolean isAuditPass;
}
