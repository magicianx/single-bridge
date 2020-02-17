package com.gaea.single.bridge.dto.app;

import com.gaea.single.bridge.enums.AuditStatus;
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
  @ApiModelProperty(value = "审核状态", required = true)
  private AuditStatus auditStatus;
}
