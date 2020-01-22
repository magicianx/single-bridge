package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.UserOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("主播列表项信息")
@Getter
@Setter
public class AnchorItemRes {
  @ApiModelProperty(value = "主播ID", required = true)
  private Long userId;

  @ApiModelProperty(value = "主播昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "等级", required = true)
  private Integer grade;

  @ApiModelProperty(value = "系统等级图标", required = true)
  private String gradeIconUrl;

  @ApiModelProperty(value = "在线状态", required = true)
  private UserOnlineStatus onlineStatus;

  @ApiModelProperty(value = "封面图片", required = true)
  private String coverUrl;

  @ApiModelProperty(value = "个性签名")
  private String signature;

  @ApiModelProperty(value = "每分钟价格", required = true)
  private Integer price;
}
