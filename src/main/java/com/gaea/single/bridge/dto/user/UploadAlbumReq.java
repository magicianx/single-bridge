package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotNull;

@ApiModel("相册上传请求")
@Getter
@Setter
public class UploadAlbumReq {
  @ApiModelProperty(value = "图片", required = true)
  @NotNull
  private FilePart img;
}
