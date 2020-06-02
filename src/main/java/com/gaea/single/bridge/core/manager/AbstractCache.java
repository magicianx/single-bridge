package com.gaea.single.bridge.core.manager;

import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCache {
  @Autowired protected RedissonReactiveClient redission;
  private static final String KEY_PREFIX = "single";

  protected String key(String key, Object identity) {
    return KEY_PREFIX + ":" + key + identity;
  }

  protected String key(String key) {
    return KEY_PREFIX + ":" + key;
  }
}
