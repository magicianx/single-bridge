package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("上报用户位置请求")
@Getter
@Setter
public class UploadUserAddressReq {
  @ApiModelProperty(value = "省份", required = true)
  @NotBlank
  private String province;

  @ApiModelProperty(value = "城市", required = true)
  @NotBlank
  private String city;

  @ApiModelProperty(value = "经度")
  private String longitude;

  @ApiModelProperty(value = "纬度")
  private String latitude;
}
