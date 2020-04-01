package com.gaea.single.bridge.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/** 编码相关工具类 */
public class Md5Utils {
  /**
   * md5加密
   *
   * @param plain 明文
   * @return 密文
   */
  public static String encrypt(String plain) {
    try {
      byte[] bytes = plain.getBytes(StandardCharsets.UTF_8.name());
      return DigestUtils.md5Hex(bytes);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("md5加密失败", e);
    }
  }
}
