package com.gaea.single.bridge.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** @author cludy */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

  /**
   * 生成随机字符串
   *
   * @return String
   */
  public static String uuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
