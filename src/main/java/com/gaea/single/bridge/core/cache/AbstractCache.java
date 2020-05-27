package com.gaea.single.bridge.core.cache;

import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCache {
  @Autowired protected RedissonReactiveClient redission;
  private static final String KEY_PREFIX = "single";

  protected String getKey(String key, Object identity) {
    return KEY_PREFIX + ":" + key + identity;
  }
}
