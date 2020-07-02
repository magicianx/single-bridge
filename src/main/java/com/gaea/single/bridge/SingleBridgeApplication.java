package com.gaea.single.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableSwagger2WebFlux
@EnableR2dbcRepositories("com.gaea.single.bridge.repository.mysql")
@EnableReactiveMongoRepositories("com.gaea.single.bridge.repository.mongodb")
@EnableScheduling
@Slf4j
public class SingleBridgeApplication implements CommandLineRunner {
  @Autowired private Environment environment;

  public static void main(String[] args) {

    SpringApplication.run(SingleBridgeApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    StandardEnvironment env = (StandardEnvironment) environment;
    List<Map> configs = new ArrayList<>();
    env.getPropertySources()
        .forEach(
            v -> {
              if (v.getName().startsWith("applicationConfig")) {
                configs.add((Map) v.getSource());
              }
            });
    log.info("--------------配置属性--------------");
    configs.forEach(v -> v.forEach((key, value) -> log.info(key + " : " + value)));
    log.info("--------------配置属性--------------");
  }
}
