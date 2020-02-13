package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("添加主播标签")
@Getter
@Setter
public class AddAnchorLabelReq {
  @ApiModelProperty(value = "主播id", required = true)
  @NotNull
  private Long anchorId;

  @ApiModelProperty(value = "标签id", required = true)
  @NotNull
  private Long labelId;
}
