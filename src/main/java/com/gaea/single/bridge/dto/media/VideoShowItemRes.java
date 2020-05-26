package com.gaea.single.bridge.dto.media;

import com.gaea.single.bridge.enums.FollowStatus;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/** @author cludy */
@Getter
@Setter
@ApiModel("视频秀信息")
public class VideoShowItemRes {
  @ApiModelProperty(value = "视频id", required = true)
  private Long videoId;

  @ApiModelProperty(value = "封面链接", required = true)
  private String coverUrl;

  @ApiModelProperty(value = "视频链接", required = true)
  private String videoUrl;

  @ApiModelProperty(value = "是否点赞", required = true)
  private Boolean isPraise;

  @ApiModelProperty(value = "点赞次数", required = true)
  private Long praiseTimes;

  @ApiModelProperty(value = "打赏礼物金额(钻石)", required = true)
  private Long gratuityMoney;

  @ApiModelProperty(value = "在线状态", required = true)
  private UserOnlineStatus onlineStatus;

  @ApiModelProperty(value = "用户信息", required = true)
  private UserInfo userInfo;

  @ApiModel("视频秀用户信息")
  @Getter
  @Setter
  public static class UserInfo {
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "头像链接", required = true)
    private String portraitUrl;

    @ApiModelProperty(value = "等级图标", required = true)
    private String gradeIcon;

    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "关注状态", required = true)
    private FollowStatus followStatus;
  }
}
