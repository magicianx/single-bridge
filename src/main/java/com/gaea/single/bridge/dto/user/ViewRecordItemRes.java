package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/** @author cludy */
@ApiModel("查看记录项信息")
@Getter
@Setter
public class ViewRecordItemRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "等级图标", required = true)
  private String gradeIconUrl;

  @ApiModelProperty(value = "是否为vip", required = true)
  private Boolean isVip;

  @ApiModelProperty(value = "云信id", required = true)
  private String yunXinId;

  @ApiModelProperty(value = "查看时的时间戳", required = true)
  private Long viewTimestamp;
}
