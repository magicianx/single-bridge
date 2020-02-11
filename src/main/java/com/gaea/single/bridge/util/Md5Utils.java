package com.gaea.single.bridge.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Md5Utils {
  public static String encrypt(String text) {
    try {
      byte[] bytes = text.getBytes(StandardCharsets.UTF_8.name());
      return DigestUtils.md5Hex(bytes);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("md5加密失败", e);
    }
  }
}
