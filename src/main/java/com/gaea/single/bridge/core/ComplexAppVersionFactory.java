package com.gaea.single.bridge.core;

import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.util.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ComplexAppVersionFactory {
  @Autowired private ServiceProperties serviceProperties;

  private ConcurrentHashMap<String, String> versionMap = new ConcurrentHashMap<>();

  public String getAppVersion(String encodeAppVersion) {
    return versionMap.computeIfAbsent(
        encodeAppVersion,
        (key) -> {
          try {
            return AESUtils.decrypt(
                key,
                serviceProperties.getAppVersion().getEncryptKey(),
                serviceProperties.getAppVersion().getEncryptIv());
          } catch (Exception e) {
            log.error("解密app version失败", e);
          }
          return "";
        });
  }
}
