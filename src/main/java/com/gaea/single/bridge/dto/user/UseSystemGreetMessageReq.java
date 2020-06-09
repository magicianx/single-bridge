package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** @author cludy */
@ApiModel("使用系统打招呼消息请求")
@Getter
@Setter
public class UseSystemGreetMessageReq {
  @ApiModelProperty(value = "消息id", required = true)
  @NotBlank
  private String messageId;

  @ApiModelProperty(value = "是否使用", required = true)
  @NotNull
  private Boolean isUse;
}
