package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 举报类型 */
@AllArgsConstructor
@Getter
public enum ReportType implements Metadata {
  IN_VIDEO(1, "视频内投诉"),
  OUT_VIDEO(2, "视频外投诉");
  private int code;
  private String desc;
}
