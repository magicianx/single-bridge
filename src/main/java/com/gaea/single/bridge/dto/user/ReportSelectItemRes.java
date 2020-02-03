package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("举报选项")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReportSelectItemRes {
  @ApiModelProperty(value = "举报原因", required = true)
  private String reason;

  @ApiModelProperty(value = "是否被默认选中", required = true)
  private Boolean isChecked;
}
