package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotNull;

@ApiModel("主播认证请求")
@Getter
@Setter
public class AnchorAuthReq {
  @ApiModelProperty(value = "认证数字", required = true)
  @NotNull
  private Integer number;

  @ApiModelProperty(value = "头像", required = true)
  @NotNull
  private FilePart portrait;

  @ApiModelProperty(value = "认证视频", required = true)
  @NonNull
  private FilePart video;
}
