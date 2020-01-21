package com.gaea.single.bridge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoboResult {
  private Integer resultCode;
  private Object dataCollection;
  private Integer totalPage;
  private Integer totalRow;
  private Integer currentPage;
}
