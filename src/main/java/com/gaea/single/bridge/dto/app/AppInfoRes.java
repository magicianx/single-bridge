package com.gaea.single.bridge.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("应用信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoRes {
  @ApiModelProperty(value = "应用版本号", required = true)
  private String appId;

  @ApiModelProperty(value = "是否审核通过, 0 时未审核通过， 1234567890 时审核通过", required = true)
  private Integer an;
}