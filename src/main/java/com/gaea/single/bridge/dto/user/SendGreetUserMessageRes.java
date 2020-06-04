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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发送打招呼响应信息")
public class SendGreetUserMessageRes {
  @ApiModelProperty(value = "距离下次发送剩余秒数", required = true)
  private Integer remainingSeconds;

  @ApiModelProperty(value = "打招呼用户消息列表", required = true)
  private List<GreetUserMessage> greetUsers;

  @Getter
  @Setter
  @ApiModel("打招呼用户消息")
  public static class GreetUserMessage {
    private Long userId;
    private String message;
    private String nickName;
    private String portraitUrl;
    private String yunXinId;
    private Boolean isVip;
  }
}
