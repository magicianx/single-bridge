package com.gaea.single.bridge.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * redis 配置
 *
 * @author cludy
 */
@Configuration
public class RedisConfig {
  private static final String REDIS_PREFIX = "redis://";
  private static final String REDIS_SSL_PREFIX = "rediss://";

  @ConfigurationProperties("spring.redis.single")
  @Bean
  RedisProperties singleRedisProperties() {
    return new RedisProperties();
  }

  @Bean
  @ConfigurationProperties("spring.redis.lobo")
  RedisProperties loboRedisProperties() {
    return new RedisProperties();
  }

  @Bean
  @Primary
  public RedissonReactiveClient singleRedissonReactiveClient(
      @Qualifier("singleRedisProperties") RedisProperties redisProperties) {
    return newRedissonClient(redisProperties);
  }

//  @Bean
//  public RedissonReactiveClient loboRedissonReactiveClient(
//      @Qualifier("loboRedisProperties") RedisProperties redisProperties) {
//    return newRedissonClient(redisProperties);
//  }

  private RedissonReactiveClient newRedissonClient(RedisProperties redisProperties) {
    Config config = new Config();

    // 超时时间（毫秒）
    int timeout =
        redisProperties.getTimeout() == null
            ? 10000
            : (int) redisProperties.getTimeout().toMillis();

    if (redisProperties.getSentinel() != null) {
      String[] nodes = convert(redisProperties.getSentinel().getNodes());
      config
          .useSentinelServers()
          .setMasterName(redisProperties.getSentinel().getMaster())
          .addSentinelAddress(nodes)
          .setDatabase(redisProperties.getDatabase())
          .setConnectTimeout(timeout)
          .setPassword(redisProperties.getPassword());
    } else if (redisProperties.getCluster() != null) {
      String[] nodes = convert(redisProperties.getCluster().getNodes());
      config
          .useClusterServers()
          .addNodeAddress(nodes)
          .setConnectTimeout(timeout)
          .setPassword(redisProperties.getPassword());
    } else {
      String prefix = redisProperties.isSsl() ? REDIS_SSL_PREFIX : REDIS_PREFIX;
      config
          .useSingleServer()
          .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
          .setConnectTimeout(timeout)
          .setDatabase(redisProperties.getDatabase())
          .setPassword(redisProperties.getPassword());
    }
    return Redisson.createReactive(config);
  }

  private String[] convert(List<String> nodesObject) {
    return nodesObject.stream()
        .map(
            node -> {
              if (!node.startsWith(REDIS_PREFIX) && !node.startsWith(REDIS_SSL_PREFIX)) {
                return REDIS_PREFIX + node;
              } else {
                return node;
              }
            })
        .toArray(String[]::new);
  }
}
