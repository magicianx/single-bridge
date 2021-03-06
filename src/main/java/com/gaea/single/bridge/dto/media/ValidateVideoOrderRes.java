package com.gaea.single.bridge.dto.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("验证视频订单响应")
public class ValidateVideoOrderRes {
  @ApiModelProperty(value = "订单临时id", required = true)
  private String orderTempId;

  @ApiModelProperty(value = "主叫方昵称", required = true)
  private String callerNickName;

  @ApiModelProperty(value = "主叫方等级图标url", required = true)
  private String callerGradeUrl;

  @ApiModelProperty(value = "主叫方等级", required = true)
  private Integer callerGrade;

  @ApiModelProperty(value = "主叫方头像url", required = true)
  private String callerPortraitUrl;

  @ApiModelProperty(value = "主叫方显示id", required = true)
  private Long callerShowId;

  @ApiModelProperty(value = "主叫方用户id", required = true)
  private Long callerUserId;

  @ApiModelProperty(value = "被叫方昵称", required = true)
  private String calledNickName;

  @ApiModelProperty(value = "被叫方等级图标url", required = true)
  private String calledGradeUrl;

  @ApiModelProperty(value = "被叫方等级", required = true)
  private Integer calledGrade;

  @ApiModelProperty(value = "被叫方头像url", required = true)
  private String calledPortraitUrl;

  @ApiModelProperty(value = "被叫方显示id", required = true)
  private Long calledShowId;

  @ApiModelProperty(value = "被叫方用户id", required = true)
  private Long calledUserId;

  @ApiModelProperty(value = "单价，单位为砖石", required = true)
  private Integer price;
}
