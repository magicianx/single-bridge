package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** @author cludy */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户打招呼消息信息")
public class GreetMessageRes {
  @ApiModelProperty(value = "消息id", required = true)
  private String id;

  @ApiModelProperty(value = "是否正在使用中", required = true)
  private Boolean isUsing;

  @ApiModelProperty(value = "消息内容", required = true)
  private String content;
}
