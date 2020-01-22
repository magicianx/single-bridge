package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@ApiModel("主播资料")
@Getter
@Setter
public class AnchorProfileRes {
  @ApiModelProperty(value = "主播id", required = true)
  private Long userId;

  @ApiModelProperty(value = "显示Id", required = true)
  private String showId;

  @ApiModelProperty(value = "云信id", required = true)
  private String yunXinId;

  @ApiModelProperty(value = "在线状态", required = true)
  private UserOnlineStatus onlineStatus;

  @ApiModelProperty(value = "昵称", required = true)
  private String nickName;

  @ApiModelProperty(value = "头像", required = true)
  private String portraitUrl;

  @ApiModelProperty(value = "等级图标链接", required = true)
  private String gradeIconUrl;

  @ApiModelProperty(value = "价格", required = true)
  private Integer price;

  @ApiModelProperty(value = "性别", required = true)
  private GenderType gender;

  @ApiModelProperty(value = "年龄", required = true)
  private Integer age;

  @ApiModelProperty(value = "城市", required = true)
  private String city;

  @ApiModelProperty("简介")
  private String intro;

  @ApiModelProperty(value = "相册列表", required = true)
  private List<String> photos;

  @ApiModelProperty(value = "标签列表", notes = "不存在时为空列表")
  private List<AnchorLabelRes> labels = new ArrayList<>();

  @ApiModelProperty(value = "收到礼物图片列表", notes = "不存在时为空列表")
  private List<String> giftIcons = new ArrayList<>();
}
