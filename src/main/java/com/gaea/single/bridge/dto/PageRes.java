package com.gaea.single.bridge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageRes<T> {
  @ApiModelProperty(value = "总页数", required = true)
  private Integer totalPage;

  @ApiModelProperty(value = "总页数", required = true)
  private Integer totalRow;

  @ApiModelProperty(value = "总页数", required = true)
  private Integer currentPage;

  private List<T> records;

  public static <T> PageRes<T> from(LoboResult loboResult, List<T> records) {
    PageRes<T> pageRes = new PageRes<>();
    pageRes.setCurrentPage(loboResult.getCurrentPage());
    pageRes.setTotalPage(loboResult.getTotalPage());
    pageRes.setTotalRow(loboResult.getTotalRow());
    pageRes.setRecords(records);
    return pageRes;
  }
}
