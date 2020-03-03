package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("微信支付信息")
public class WechatPayRes {
  @ApiModelProperty(value = "应用id", required = true)
  private String appId;

  @ApiModelProperty(value = "商户号", required = true)
  private String mchId;

  @ApiModelProperty(value = "预支付交易会话标识", required = true)
  private String prepayId;

  @ApiModelProperty(value = "随机字符串", required = true)
  private String nonceStr;

  @ApiModelProperty(value = "时间戳", required = true)
  private String timestamp;

  @ApiModelProperty(value = "签名", required = true)
  private String sign;
}
