package com.gaea.single.bridge.dto.user;

import com.gaea.single.bridge.enums.UserOnlineStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@ApiModel("用户等级响应")
@Getter
@Setter
public class UserGradeRes {
  @ApiModelProperty(value = "当前经验值", required = true)
  private Long currentXp;

  @ApiModelProperty(value = "等级经验列表", required = true)
  private List<Long> gradeXps =  new ArrayList<>();

  @ApiModelProperty(value = "置顶剩余次数", required = true)
  private Integer setTopNum;

  @ApiModelProperty(value = "置顶持续时间(秒)", required = true)
  private Integer setTopDurationTime;

  @ApiModelProperty(value = "用户在线状态, 用户空闲时可置顶, 勿扰时不可置顶", required = true)
  private UserOnlineStatus onlineStatus;
}
