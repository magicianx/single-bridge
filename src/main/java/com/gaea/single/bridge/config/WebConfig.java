package com.gaea.single.bridge.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.VersionResourceResolver;

import java.util.stream.Stream;

@Configuration
@ConditionalOnProperty(value = "service.appStaticHome")
public class WebConfig implements WebFluxConfigurer {
  @Autowired private ServiceProperties properties;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Stream.of(StaticResource.of("/config/**", "/config"), StaticResource.of("/html/**", "/html"))
        .forEach(
            resource -> {
              String location =
                  String.format(
                      "file://%s%s/", properties.getAppStaticHome(), resource.getLocation());
              registry
                  .addResourceHandler(resource.getPath())
                  .addResourceLocations(location)
                  .resourceChain(true)
                  .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
            });
  }

  @Getter
  @AllArgsConstructor
  public static class StaticResource {
    private String path;
    private String location;

    public static StaticResource of(String path, String location) {
      return new StaticResource(path, location);
    }
  }
}
