package com.gaea.single.bridge.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/** @author cludy */
@ApiModel("排行榜菜单信息")
@Getter
@Setter
@AllArgsConstructor
public class RankMenuRes {
  @ApiModelProperty(value = "菜单名称", required = true)
  private Long menuId;

  @ApiModelProperty(value = "菜单名称", required = true)
  private String name;

  @ApiModelProperty(value = "子菜单列表", required = true)
  private List<Menu> child;

  @ApiModel("子菜单信息")
  @Getter
  @Setter
  @AllArgsConstructor
  public static class Menu {
    @ApiModelProperty(value = "菜单id", required = true)
    private Long menuId;

    @ApiModelProperty(value = "菜单名称", required = true)
    private String name;
  }
}
