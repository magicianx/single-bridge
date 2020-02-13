package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.ShareWayType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("分享方式信息")
@AllArgsConstructor
@NoArgsConstructor
public class ShareWayRes {
  @ApiModelProperty(value = "类型", required = true)
  private ShareWayType type;

  @ApiModelProperty(value = "分享标题", required = true)
  private String title;

  @ApiModelProperty(value = "分享内容", required = true)
  private String content;

  @ApiModelProperty(value = "落地页链接", required = true)
  private String url;
}
