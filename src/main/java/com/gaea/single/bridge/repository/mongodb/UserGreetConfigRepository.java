package com.gaea.single.bridge.repository.mongodb;

import com.gaea.single.bridge.entity.mongodb.UserGreetConfig;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/** @author cludy */
public interface UserGreetConfigRepository
    extends ReactiveMongoRepository<UserGreetConfig, String> {
  Mono<UserGreetConfig> findByUserId(Long userId);

  Mono<Long> countByUserId(Long userId);
}
