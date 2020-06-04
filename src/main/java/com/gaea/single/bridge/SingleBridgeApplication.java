package com.gaea.single.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableSwagger2WebFlux
@EnableR2dbcRepositories("com.gaea.single.bridge.repository")
public class SingleBridgeApplication {
  public static void main(String[] args) {
    SpringApplication.run(SingleBridgeApplication.class, args);
  }
}
