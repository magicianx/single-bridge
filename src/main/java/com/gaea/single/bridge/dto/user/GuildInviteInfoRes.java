package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.GuildInviteStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** @author cludy */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("公会邀请信息")
public class GuildInviteInfoRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "用户昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "公会名称", required = true)
  private String guildName;

  @ApiModelProperty(value = "邀请状态", required = true)
  private GuildInviteStatus status;
}
