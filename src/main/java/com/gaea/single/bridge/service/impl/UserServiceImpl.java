package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.core.cache.UserCache;
import com.gaea.single.bridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** @author cludy */
@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserCache userCache;

  @Override
  public Mono<Boolean> isEnablePosition(Long userId) {
    return userCache.getPositionStatus(userId);
  }

  @Override
  public Mono<Void> switchPositionStatus(Long userId, boolean isEnable) {
    return userCache.setPositionStatus(userId, isEnable);
  }
}
