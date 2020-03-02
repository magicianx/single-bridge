package com.gaea.single.bridge.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gaea.single.bridge.SingleBridgeApplication;
import com.gaea.single.bridge.core.lobo.DefaultLoboResultExchanger;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.core.lobo.LoboResultExchanger;
import org.platform.config.ConfigAgent;
import org.platform.config.ConfigBuilder;
import org.platform.config.ConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceConfig implements WebFluxConfigurer {
  @Autowired private ServiceProperties serviceProperties;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").maxAge(3600);
  }

  @Bean
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
            .addConfigSet(SingleBridgeApplication.class.getPackage().getName());

    ConfigManager.init(builder.builder());
    return ConfigManager.get();
  }

  @Bean
  @DependsOn("configAgent")
  public WebClient loboMainWebClient() {
    String host = DictionaryProperties.get().getLobo().getMainHost();
    return WebClient.builder().baseUrl(host).build();
  }

  @Bean
  @DependsOn("configAgent")
  public WebClient appAuditWebClient() {
    String host = DictionaryProperties.get().getLobo().getAppAuditHost();
    return WebClient.builder().baseUrl(host).build();
  }

  @Bean
  @DependsOn("configAgent")
  public WebClient loboOtherWebClient() {
    String host = DictionaryProperties.get().getLobo().getOtherHost();
    return WebClient.builder().baseUrl(host).build();
  }

  @Bean
  public LoboResultExchanger loboResultExchanger() {
    return new DefaultLoboResultExchanger();
  }

  @Bean
  @Primary
  public LoboClient loboMainClient(@Autowired @Qualifier("loboMainWebClient") WebClient webClient) {
    return new LoboClient(webClient, new DefaultLoboResultExchanger());
  }

  @Bean
  public LoboClient appAuditClient(@Autowired @Qualifier("appAuditWebClient") WebClient webClient) {
    return new LoboClient(webClient, new DefaultLoboResultExchanger());
  }

  @Bean
  public LoboClient loboOtherClient(
      @Autowired @Qualifier("loboOtherWebClient") WebClient webClient) {
    return new LoboClient(webClient, new DefaultLoboResultExchanger());
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }
}
