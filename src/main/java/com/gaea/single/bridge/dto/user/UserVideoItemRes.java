package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("用户视频项信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVideoItemRes {
  @ApiModelProperty(value = "视频id", required = true)
  private Long id;

  @ApiModelProperty(value = "云文件id", required = true)
  private String cloudFileId;

  @ApiModelProperty(value = "封面链接", required = true)
  private String coverUrl;

  @ApiModelProperty(value = "视频链接", required = true)
  private String videoUrl;

  @ApiModelProperty(value = "审核状态", required = true)
  private AuditStatus auditStatus;

  @ApiModelProperty(value = "是否点赞", required = true)
  private Boolean isPraise;

  @ApiModelProperty(value = "点赞次数", required = true)
  private Long praiseTimes;
}
