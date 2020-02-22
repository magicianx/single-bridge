package com.gaea.single.bridge.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
@Getter
@Setter
public class ServiceProperties {
  private Config config;
  private String reportImgPath;
  private String appStaticHome;

  @Getter
  @Setter
  public static class Config {
    private String server;
    private String secretKey;
    private String module;
    private String branch;
    private String group;
  }
}
