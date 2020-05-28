package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/** @author cludy */
@ApiModel("添加打招呼信息请求")
@Getter
@Setter
public class DeleteGreetMessageReq {
  @ApiModelProperty(value = "消息内容", required = true)
  @NotBlank
  private String message;
}
