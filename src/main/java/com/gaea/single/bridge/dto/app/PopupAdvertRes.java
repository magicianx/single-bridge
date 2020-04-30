package com.gaea.single.bridge.dto.app;

import com.gaea.single.bridge.enums.AdvertType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("弹窗广告")
@AllArgsConstructor
@NoArgsConstructor
public class PopupAdvertRes {
  @ApiModelProperty(value = "标题", required = true)
  private String title;

  @ApiModelProperty(value = "广告类型", required = true)
  private AdvertType type;

  @ApiModelProperty(value = "图片链接", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "跳转链接", required = true)
  private String link;
}
