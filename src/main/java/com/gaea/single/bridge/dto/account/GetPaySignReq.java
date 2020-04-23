package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.PayScene;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("获取支付签名")
public class GetPaySignReq {
  @ApiModelProperty(value = "支付选项id", required = true)
  @NotNull
  private Long optionId;

  @ApiModelProperty(value = "支付金额（钻石）", required = true)
  @NotNull
  private Integer diamonds;

  @ApiModelProperty(value = "支付场景", required = true)
  @NotNull
  private PayScene scene;
}
