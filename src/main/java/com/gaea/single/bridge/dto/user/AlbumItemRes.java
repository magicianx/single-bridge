package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("相册信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumItemRes {
  @ApiModelProperty(value = "图片链接", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "审核状态", required = true)
  private AuditStatus status;

  @ApiModelProperty(value = "是否为封面", required = true)
  private Boolean isCover;
}
