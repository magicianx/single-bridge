package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** @author cludy */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("打招呼状态信息")
public class GreetStatusRes {
  @ApiModelProperty("是否启用")
  private Boolean isEnable;

  @ApiModelProperty("距离下次发送剩余秒数")
  private Integer remainingSeconds;
}
