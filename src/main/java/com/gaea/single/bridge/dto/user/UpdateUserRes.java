package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

@ApiModel("编辑用户信息资料请求")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRes {
  @ApiModelProperty(value = "昵称", required = true)
  String portraitUrl;
}
