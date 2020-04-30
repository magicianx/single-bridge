package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("搜索用户结果项信息")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserItemRes {
  @ApiModelProperty(value = "用户ID", required = true)
  private Long userId;

  @ApiModelProperty(value = "头像链接", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;
}
