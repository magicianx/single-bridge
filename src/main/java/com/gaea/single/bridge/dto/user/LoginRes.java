package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("登录响应")
public class LoginRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long id;

  @ApiModelProperty(value = "显示id", required = true)
  private String showId;

  @ApiModelProperty(value = "云信id", required = true)
  private String yunXinId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "session", required = true)
  private String session;

  @ApiModelProperty(value = "头像路径", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "简介")
  private String intro;

  @ApiModelProperty(value = "账户余额", required = true)
  private Long balance;

  @ApiModelProperty(value = "性别")
  private GenderType gender;

  @ApiModelProperty(value = "生日")
  private String birthday;

  @ApiModelProperty(value = "是否完善生日", required = true)
  private Boolean isPerfectBirthday;

  @ApiModelProperty(value = "是否完善性别", required = true)
  private Boolean isPerfectGender;

  @ApiModelProperty(value = "是否完善相册", required = true)
  private Boolean isAlbumPerfect;

  @ApiModelProperty(value = "是否完善视频", required = true)
  private Boolean isVideoPerfect;
}
