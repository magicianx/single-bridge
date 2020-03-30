package com.gaea.single.bridge.dto.platform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("引流包弹窗广告")
@AllArgsConstructor
@NoArgsConstructor
public class DpPopupAdvertRes {
  @ApiModelProperty(value = "广告图片链接", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "下载链接", required = true)
  private String url;
}
