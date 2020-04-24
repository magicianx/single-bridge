package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.FollowStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("关注项信息")
@Getter
@Setter
public class FollowItemRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "等级", required = true)
  private Integer grade;

  @ApiModelProperty(value = "等级头像链接", required = true)
  private String gradeHeadUrl;

  @ApiModelProperty("简介")
  private String intro;

  @ApiModelProperty(value = "关注状态", required = true)
  private FollowStatus followStatus;
}
