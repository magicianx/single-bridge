package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel("编辑用户信息资料请求")
@Getter
@Setter
public class UpdateUserReq {
  @ApiModelProperty(value = "昵称", required = true)
  String nickName;

  @ApiModelProperty(value = "个人签名", required = true)
  String intro;

  @ApiModelProperty(value = "性别", required = true)
  String gender;

  @ApiModelProperty(value = "生日", required = true)
  String birthday;

  @ApiModelProperty(value = "头像列表", required = true)
  List<FilePart> port;
}
