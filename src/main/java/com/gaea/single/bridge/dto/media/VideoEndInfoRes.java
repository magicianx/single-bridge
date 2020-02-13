package com.gaea.single.bridge.dto.media;

import com.gaea.single.bridge.dto.user.LabelRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("视频结束信息请求")
public class VideoEndInfoRes {
  @ApiModelProperty(value = "视频持续时间", required = true)
  private Integer durationTime;

  @ApiModelProperty(value = "花费钻石", required = true)
  private Integer spentDiamonds;

  @ApiModelProperty(value = "主播信息", required = true)
  public Anchor anchor;

  @ApiModelProperty(value = "标签列表", required = true)
  private List<LabelRes> labels;

  @ApiModelProperty(value = "推荐主播列表", required = true)
  private List<RecommendAnchor> recommendAnchors;

  @ApiModel("主播信息")
  @Getter
  @Setter
  public static class Anchor {
    @ApiModelProperty(value = "主播用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "显示id", required = true)
    private String showId;

    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    @ApiModelProperty(value = "头像链接", required = true)
    private String portraitUrl;

    @ApiModelProperty(value = "等级", required = true)
    private Integer grade;

    @ApiModelProperty(value = "等级图标链接", required = true)
    private String gradeIconUrl;
  }

  @ApiModel("推荐主播信息")
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class RecommendAnchor {
    @ApiModelProperty("主播用户id")
    private Long id;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像链接")
    private String portraitUrl;
  }
}
