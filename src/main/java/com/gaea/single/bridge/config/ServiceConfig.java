package com.gaea.single.bridge.config;

import com.gaea.single.bridge.support.lobo.LoboClient;
import com.gaea.single.bridge.support.lobo.DefaultLoboResultExchanger;
import org.platform.config.ConfigAgent;
import org.platform.config.ConfigBuilder;
import org.platform.config.ConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceConfig {
  @Autowired private ServiceProperties serviceProperties;

  //  @Bean
  public ConfigAgent configAgent(@Value("${spring.application.name}") String name)
      throws Exception {
    ServiceProperties.Config config = serviceProperties.getConfig();
    ConfigBuilder builder =
        ConfigBuilder.build()
            .serverPath(config.getServer())
            .secretKey(config.getSecretKey())
            .group(config.getGroup())
            .module(config.getModule())
            .branch(config.getBranch())
            .agentId(name)
            .appendParam("delay", 10)
            .appendParam("gap", 60)
            .addConfigSet("com.gaea.single.bridge.config");
    ConfigManager.init(builder);
    return ConfigManager.get();
  }

  @Bean
  //  @DependsOn("configAgent")
  public WebClient webClient() {
    String host = DictionaryProperties.get().getLobo().getHost();
    return WebClient.builder().baseUrl(host).build();
  }

  @Bean
  public LoboClient loboClient(WebClient webClients) {
    return new LoboClient(webClients, new DefaultLoboResultExchanger());
  }
}
