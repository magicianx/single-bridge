package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.OsType;
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

  @ApiModelProperty("包名, h5支付时必传，原生通过请求头传递")
  private String packageName;

  @ApiModelProperty("版本号, h5支付时必传，原生通过请求头传递")
  private String version;

  @ApiModelProperty("系统类型, h5支付时必传，原生通过请求头传递")
  private OsType osType;

  @ApiModelProperty("session, h5支付时必传，原生通过请求头传递")
  private String session;

  @ApiModelProperty("用户id, h5支付时必传，原生通过请求头传递")
  private String userId;
}
