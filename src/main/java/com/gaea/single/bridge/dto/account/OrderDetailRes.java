package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.OrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("订单详情")
public class OrderDetailRes {
  @ApiModelProperty(value = "订单号", required = true)
  private String orderNo;

  @ApiModelProperty(value = "订单类型", required = true)
  private OrderType type;

  @ApiModelProperty(value = "订单标题", required = true)
  private String title;

  @ApiModelProperty(value = "发生金额(钻石)", required = true)
  private String amount;

  @ApiModelProperty(value = "视频通话时长(秒), 当类型为VIDEO_PAY或VIDEO_INCOME有值")
  private Integer videoDurationSecond;

  @ApiModelProperty(value = "发生时间", required = true)
  private String time;

  @ApiModelProperty(value = "交易用户id", required = true)
  private Long tradeUserId;

  @ApiModelProperty(value = "交易用户的显示id", required = true)
  private String tradeUserShowId;

  @ApiModelProperty(value = "交易用户的昵称", required = true)
  private String tradeUserNickName;

  @ApiModelProperty(value = "交易用户的头像链接", required = true)
  private String tradeUserPortraitUrl;
}
