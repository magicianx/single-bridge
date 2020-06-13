package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.UserOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** @author cludy */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("排行榜信息")
public class RankingRes {
  @ApiModelProperty(value = "提示文案")
  private String tipText;

  @ApiModelProperty(value = "用户列表", required = true)
  private List<RankUser> users;

  @Getter
  @Setter
  @ApiModel("排名用户信息")
  public static class RankUser {
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
}
