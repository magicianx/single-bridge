package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

@ApiModel("编辑用户信息资料请求")
@Getter
@Setter
public class UpdateUserReq {
  @ApiModelProperty(value = "昵称")
  private String nickName;

  @ApiModelProperty(value = "个人签名")
  private String intro;

  @ApiModelProperty(value = "性别")
  private GenderType gender;

  @ApiModelProperty(value = "生日")
  private String birthday;

  @ApiModelProperty(value = "头像")
  private FilePart portrait;
}
