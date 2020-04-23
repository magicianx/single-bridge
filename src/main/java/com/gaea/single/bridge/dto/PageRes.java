package com.gaea.single.bridge.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
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

  public static <T> PageRes<T> empty() {
    PageRes<T> pageRes = new PageRes<>();
    pageRes.setCurrentPage(1);
    pageRes.setTotalPage(0);
    pageRes.setTotalRow(0);
    pageRes.setRecords(Collections.emptyList());
    return pageRes;
  }

  public static <T> PageRes<T> from(LoboResult loboResult, List<T> records) {
    PageRes<T> pageRes = new PageRes<>();
    pageRes.setCurrentPage(loboResult.getCurrentPage());
    pageRes.setTotalPage(loboResult.getTotalPage());
    pageRes.setTotalRow(loboResult.getTotalRow());
    pageRes.setRecords(records);
    return pageRes;
  }

  public static <T> PageRes<T> of(
      Integer totalPage, Integer currentPage, Integer totalRow, List<T> records) {
    PageRes<T> pageRes = new PageRes<>();
    pageRes.setCurrentPage(currentPage);
    pageRes.setTotalPage(totalPage);
    pageRes.setTotalRow(totalRow);
    pageRes.setRecords(records);
    return pageRes;
  }
}
