package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("删除视频请求")
@Getter
@Setter
public class DeleteVideoReq {
  @ApiModelProperty(value = "云文件id", required = true)
  @NotNull
  private String cloudFileId;

  @ApiModelProperty(value = "视频审核状态", required = true)
  @NotNull
  private AuditStatus auditStatus;
}
