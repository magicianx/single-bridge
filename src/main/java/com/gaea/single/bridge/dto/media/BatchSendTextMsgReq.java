package com.gaea.single.bridge.dto.media;

import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("批量发送文本消息请求")
public class BatchSendTextMsgReq {
  @ApiModelProperty(value = "发送密钥", required = true)
  private String secret;

  @ApiModelProperty(value = "操作系统类型", required = true)
  private OsType osType;

  @ApiModelProperty(value = "用户类型", required = true)
  private UserType userType;

  @ApiModelProperty(value = "消息内容", required = true)
  private String content;
}
