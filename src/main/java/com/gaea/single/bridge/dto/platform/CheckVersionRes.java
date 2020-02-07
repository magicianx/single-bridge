package com.gaea.single.bridge.dto.platform;

import com.gaea.single.bridge.enums.VersionUpdateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("检查更新响应信息")
public class CheckVersionRes {
  @ApiModelProperty(value = "版本编号", required = true)
  private int code;

  @ApiModelProperty(value = "版本号", required = true)
  private String version;

  @ApiModelProperty(value = "更新类型", required = true)
  private VersionUpdateType type;

  @ApiModelProperty(value = "更新详情", required = true)
  private List<String> details;

  @ApiModelProperty(value = "更新链接", required = true)
  private String updateUrl;
}
