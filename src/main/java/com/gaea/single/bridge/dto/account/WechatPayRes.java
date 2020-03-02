package com.gaea.single.bridge.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatPayRes {
  @JsonProperty("appid")
  private String appId;

  @JsonProperty("partnerid")
  private String partnerId;

  @JsonProperty("prepayid")
  private String prepayId;

  @JsonProperty("package")
  private String packageName;

  @JsonProperty("noncestr")
  private String nonceStr;

  private String timestamp;
  private String sign;
}
