package com.gaea.single.bridge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.VersionResourceResolver;

@Configuration
public class WebConfig implements WebFluxConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/html/**")
        .addResourceLocations("file:///data/app/single/html")
        .resourceChain(true)
        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
  }
}
