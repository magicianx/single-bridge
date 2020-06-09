package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** @author cludy */
@Getter
@Setter
@ApiModel("用户打招呼配置信息")
@AllArgsConstructor
@NoArgsConstructor
public class UserGreetConfigRes {
  @ApiModelProperty(value = "系统消息列表", required = true)
  private List<GreetMessageRes> systemMessages;

  @ApiModelProperty(value = "自定义消息列表", required = true)
  private List<GreetMessageRes> customMessages;
}
