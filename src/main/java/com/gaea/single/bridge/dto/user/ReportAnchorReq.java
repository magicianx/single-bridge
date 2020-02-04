package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("举报请求")
@Getter
@Setter
public class ReportAnchorReq {
  @ApiModelProperty(value = "被举报用户id", required = true)
  @NotNull
  private Long reportedUserId;

  @ApiModelProperty(value = "被举原因", required = true)
  @NotBlank
  private String reason;

//  @ApiModelProperty(value = "举报图片", required = true)
//  @NotNull
//  private FilePart img;

  @ApiModelProperty(value = "是否拉黑", required = true)
  @NotNull
  private Boolean isPullBack;
}
