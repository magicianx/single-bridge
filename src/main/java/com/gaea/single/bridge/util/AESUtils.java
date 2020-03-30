package com.gaea.single.bridge.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/** AES加解密工具类 */
@SuppressWarnings("Duplicates")
public class AESUtils {
  private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String KEY_ALGORITHM = "AES";

  /**
   * aes加密
   *
   * @param plaintext 明文
   * @param key 密钥
   * @return 密文
   * @throws Exception
   */
  public static String encrypt(String plaintext, String key, String iv) throws Exception {
    IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    byte[] bytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * aes解密
   *
   * @param cipherText 密文
   * @param key 密钥
   * @return 明文
   * @throws Exception
   */
  public static String decrypt(String cipherText, String key, String iv) throws Exception {
    IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
    return new String(bytes, StandardCharsets.UTF_8);
  }

  /**
   * 生成密钥
   *
   * @return 密钥
   * @throws NoSuchAlgorithmException
   */
  public static String generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
    keyGenerator.init(128);
    SecretKey key = keyGenerator.generateKey();
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  /**
   * 生成iv
   *
   * @return iv
   */
  public static String generateIv() {
    byte[] iv = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    return Base64.getEncoder().encodeToString(iv);
  }

  public static void main(String[] args) throws Exception {
    String key = generateKey();
    String iv = generateIv();
    System.out.println("key: " + key);
    System.out.println("iv: " + iv);
    String cipherText = encrypt("哈哈哈", key, iv);
    System.out.println(cipherText);
    System.out.println(decrypt(cipherText, key, iv));
  }
}
