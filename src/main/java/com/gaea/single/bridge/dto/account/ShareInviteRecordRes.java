package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("分享邀请记录")
public class ShareInviteRecordRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "收益(元)", required = true)
  private Long money;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;
}
