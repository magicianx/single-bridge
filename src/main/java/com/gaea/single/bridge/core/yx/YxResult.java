package com.gaea.single.bridge.core.yx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YxResult {
  private String code;
  private String timetag;

  public boolean isSuccess() {
    return "200".equals(code);
  }
}
