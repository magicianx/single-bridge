package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AnchorAuthStatus;
import com.gaea.single.bridge.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("用户信息")
@Getter
@Setter
public class UserInfoRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long id;

  @ApiModelProperty(value = "显示id", required = true)
  private String showId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像路径", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "简介")
  private String intro;

  @ApiModelProperty(value = "等级", required = true)
  private Integer grade;

  @ApiModelProperty(value = "等级图标", required = true)
  private String gradeIconUrl;

  @ApiModelProperty(value = "手机号", notes = "被*遮盖后的手机号")
  private String mobilePhone;

  @ApiModelProperty(value = "账户余额", required = true)
  private Long balance;

  @ApiModelProperty(value = "是否完善生日", required = true)
  private Boolean isPerfectBirthday;

  @ApiModelProperty(value = "是否完善性别", required = true)
  private Boolean isPerfectGender;

  @ApiModelProperty(value = "用户类型", required = true)
  private UserType userType;

  @ApiModelProperty(value = "邀请码", required = true)
  private String inviteCode;

  @ApiModelProperty(value = "粉丝数量", required = true)
  private Integer fansNum;

  @ApiModelProperty(value = "关注数量", required = true)
  private Integer followNum;

  @ApiModelProperty(value = "主播认证状态", required = true)
  private AnchorAuthStatus authStatus;

  @ApiModelProperty(value = "是否为vip", required = true)
  private Boolean isVip;

  @ApiModelProperty(value = "剩余发送消息次数", required = true)
  private Integer messageCount;

  @ApiModelProperty(value = "是否绑定手机号", required = true)
  private Boolean isBindPhone;
}
