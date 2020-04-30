package com.gaea.single.bridge.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** sha1工具类 */
public class Sha1Utils {
  private static final String ALGORITHM = "sha1";

  private static final char[] HEX_DIGITS = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  /**
   * sha1签名
   *
   * @param plaintext 明文
   * @return 签名
   */
  public static String digest(String plaintext) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      messageDigest.update(plaintext.getBytes(StandardCharsets.UTF_8));
      return getFormattedText(messageDigest.digest());
    } catch (Exception e) {
      throw new RuntimeException("sha1 签名失败", e);
    }
  }

  private static String getFormattedText(byte[] bytes) {
    int len = bytes.length;
    StringBuilder buf = new StringBuilder(len * 2);
    for (byte aByte : bytes) {
      buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
      buf.append(HEX_DIGITS[aByte & 0x0f]);
    }
    return buf.toString();
  }
}
