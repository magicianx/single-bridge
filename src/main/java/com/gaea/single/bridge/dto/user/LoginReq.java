package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.LoginType;
import com.gaea.single.bridge.enums.OsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("登录类型")
@Getter
@Setter
public class LoginReq {
  @ApiModelProperty(value = "登录类型", required = true)
  @NotNull
  private LoginType type;

  @ApiModelProperty("第三方ID, 第三方登录时必填")
  private String openId;

  @ApiModelProperty("第三方昵称, 第三方登录时必填")
  private String nickName;

  @ApiModelProperty("第三方头像链接, 第三方登录时必填")
  private String portraitUrl;

  @ApiModelProperty("第三方accessToken, 第三方登录时必填")
  private String accessToken;

  @ApiModelProperty("手机号, 手机号登录必填")
  private String phoneNum;

  @ApiModelProperty("密码, 手机号密码登录必填")
  private String password;

  @ApiModelProperty(value = "操作系统", required = true)
  @NotNull
  private OsType os;

  @ApiModelProperty(value = "设备号", required = true)
  @NotBlank
  private String deviceNo;

  @ApiModelProperty(value = "包名", required = true)
  @NotBlank
  private String packageName;
}
