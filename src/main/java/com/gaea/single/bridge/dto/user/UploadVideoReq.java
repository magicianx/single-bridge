package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("上传视频请求")
@Getter
@Setter
public class UploadVideoReq {
  @ApiModelProperty(value = "封面链接", required = true)
  @NotBlank
  private String coverUrl;

  @ApiModelProperty(value = "云文件id", required = true)
  @NotNull
  private String cloudFileId;

  @ApiModelProperty(value = "视频链接", required = true)
  @NotNull
  private String videoUrl;
}
