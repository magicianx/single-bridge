package com.gaea.single.bridge.dto.media;

import com.gaea.single.bridge.enums.StartOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("开始视频订单请求")
public class StartVideoOrderReq {
  @ApiModelProperty(value = "订单临时id", required = true)
  @NotBlank
  private String orderTempId;

  @ApiModelProperty(value = "请求类型", required = true)
  @NotNull
  private StartOrderType type;
}
