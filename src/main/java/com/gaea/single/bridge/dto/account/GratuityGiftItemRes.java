package com.gaea.single.bridge.dto.account;

import com.gaea.single.bridge.enums.GiftType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("打赏礼物项信息")
@Getter
@Setter
public class GratuityGiftItemRes {
  @ApiModelProperty(value = "礼物id", required = true)
  private Long id;

  @ApiModelProperty(value = "礼物金额", required = true)
  private Long amount;

  @ApiModelProperty(value = "礼物名称", required = true)
  private String name;

  @ApiModelProperty(value = "礼物大图", required = true)
  private String imgUrl;

  @ApiModelProperty(value = "礼物小图", required = true)
  private String smallImgUrl;

  @ApiModelProperty(value = "用户最小等级限制", required = true)
  private Integer minGrade;

  @ApiModelProperty("svg动画文件, 当此字段有值时出现动画")
  private String svgUrl;

  @ApiModelProperty(value = "礼物类型", required = true)
  private GiftType type;
}
