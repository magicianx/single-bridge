package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.enums.UserOnlineStatus;
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

  @ApiModelProperty(value = "云信id", required = true)
  private String yunXinId;

  @ApiModelProperty(value = "session", required = true)
  private String session;

  @ApiModelProperty(value = "性别")
  private GenderType gender;

  @ApiModelProperty(value = "生日")
  private String birthday;

  @ApiModelProperty(value = "在线状态", required = true)
  private UserOnlineStatus onlineStatus;
}
