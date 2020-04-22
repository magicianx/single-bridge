package com.gaea.single.bridge.core.cache;

import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMessageCache {
  @Autowired protected RedissonReactiveClient redission;
  private static final String KEY_PREFIX = "single";

  abstract String getKeyName();

  protected String getKey(Object identity) {
    return KEY_PREFIX + ":" + getKeyName() + ":" + identity;
  }
}
