package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.core.manager.UserManager;
import com.gaea.single.bridge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** @author cludy */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired private UserManager userManager;

  @Override
  public Mono<Boolean> isEnablePosition(Long userId) {
    return userManager.getPositionStatus(userId);
  }

  @Override
  public Mono<Void> switchPositionStatus(Long userId, boolean isEnable) {
    return userManager.setPositionStatus(userId, isEnable);
  }
}
