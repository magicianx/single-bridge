package com.gaea.single.bridge.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("分页请求")
@Getter
@Setter
public class  PageReq {
  @ApiModelProperty(value = "第几页, 从1开始", required = true)
  @NotNull
  private Integer pageNum;

  @ApiModelProperty(value = "每页条数", required = true)
  @NotNull
  private Integer pageSize;
}
