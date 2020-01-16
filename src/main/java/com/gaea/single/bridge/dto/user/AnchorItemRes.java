package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AnchorOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("主播列表项信息")
@Getter
@Setter
public class AnchorItemRes {
  @ApiModelProperty(value = "主播ID", required = true)
  private String userId;

  @ApiModelProperty(value = "主播昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "等级", required = true)
  private Integer grade;

  @ApiModelProperty(value = "系统等级图标", required = true)
  private String gradeIcon;

  @ApiModelProperty(value = "在线状态", required = true)
  private AnchorOnlineStatus onlineStatus;

  @ApiModelProperty(value = "封面图片", required = true)
  private String coverUrl;

  @ApiModelProperty(value = "个性签名", required = true)
  private String signature;
}
