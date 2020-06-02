package com.gaea.single.bridge.core.manager;

import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractCache {
  @Autowired protected RedissonReactiveClient singleRedission;

  @Qualifier("loboRedissonReactiveClient")
  @Autowired
  protected RedissonReactiveClient loboRedission;

  private static final String KEY_PREFIX = "single";

  protected String key(String key, Object identity) {
    return KEY_PREFIX + ":" + key + identity;
  }

  protected String key(String key) {
    return KEY_PREFIX + ":" + key;
  }
}
