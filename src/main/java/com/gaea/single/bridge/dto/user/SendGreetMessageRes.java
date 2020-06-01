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
@ApiModel("发送打招呼响应信息")
public class SendGreetMessageRes {
  @ApiModelProperty("距离下次发送剩余秒数")
  private Integer remainingSeconds;
}
