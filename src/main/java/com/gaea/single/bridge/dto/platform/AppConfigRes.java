package com.gaea.single.bridge.dto.platform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApiModel("APP应用配置")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigRes {
  @ApiModelProperty(value = "应用配置路径", required = true)
  private List<String> configUrls;
}
