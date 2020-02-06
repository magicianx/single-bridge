package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("编辑用户信息资料请求")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRes {
  @ApiModelProperty(value = "昵称")
  private String portraitUrl;

  @ApiModelProperty(value = "头像审核状态")
  private AuditStatus portraitStatus;
}
