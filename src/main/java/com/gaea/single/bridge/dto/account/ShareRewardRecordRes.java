package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.ShareRewardType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("分享奖励信息")
public class ShareRewardRecordRes {
  @ApiModelProperty(value = "用户id", required = true)
  private Long userId;

  @ApiModelProperty(value = "类型", required = true)
  private ShareRewardType type;

  @ApiModelProperty(value = "奖励金额(元)", required = true)
  private Long money;

  @ApiModelProperty(value = "创建时间", required = true)
  private Long createTime;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;
}
