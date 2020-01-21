package com.gaea.single.bridge.dto.platform;

import com.gaea.single.bridge.enums.BannerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("广告信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerRes {
  @ApiModelProperty(value = "广告图片连接", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "跳转连接", required = true)
  private String link;

  @ApiModelProperty(value = "广告类型", required = true)
  private BannerType type;
}
