package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private DatabaseClient client;

  public Mono<Boolean> isInRegisterChannel(Long userId, String channel) {
    return client
        .execute("select count(*) from user_reg_info where channel = ? and id = ?")
        .bind(0, channel)
        .bind(1, userId)
        .as(Integer.class)
        .fetch()
        .one()
        .map(count -> count == 1);
  }
}
