package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("删除相册图片")
@Getter
@Setter
public class DeleteAlbumReq {
  @ApiModelProperty(value = "图片链接", required = true)
  @NotBlank
  private String imgUrl;

  @ApiModelProperty(value = "审核状态", required = true)
  @NotNull
  private AuditStatus status;
}
