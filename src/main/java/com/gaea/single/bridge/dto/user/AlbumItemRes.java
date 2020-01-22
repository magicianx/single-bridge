package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("相册信息")
@Getter
@Setter
public class AlbumItemRes {
  @ApiModelProperty(value = "图片链接", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "审核状态", required = true)
  private AuditStatus status;
}
