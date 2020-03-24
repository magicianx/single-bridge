package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("分享排名奖励")
@AllArgsConstructor
public class ShareRankingRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "奖励金额(元)", required = true)
  private Long money;
}
