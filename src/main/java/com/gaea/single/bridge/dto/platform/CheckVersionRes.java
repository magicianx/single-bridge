package com.gaea.single.bridge.dto.platform;

import com.gaea.single.bridge.enums.VersionUpdateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("检查更新响应信息")
@AllArgsConstructor
@NoArgsConstructor
public class CheckVersionRes {
  @ApiModelProperty(value = "版本编号", required = true)
  private int code;

  @ApiModelProperty(value = "版本号", required = true)
  private String version;

  @ApiModelProperty(value = "更新类型", required = true)
  private VersionUpdateType type;
}
