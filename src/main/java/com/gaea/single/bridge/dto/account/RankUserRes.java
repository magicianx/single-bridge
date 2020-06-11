package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.UserOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("排名用户信息")
public class RankUserRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "排名", required = true)
  private Integer ranking;

  @ApiModelProperty(value = "在线状态", required = true)
  private UserOnlineStatus onlineStatus;

  @ApiModelProperty(value = "等级图标链接", required = true)
  private String gradeIconUrl;

  @ApiModelProperty(value = "是否为vip", required = true)
  private Boolean isVip;
}
