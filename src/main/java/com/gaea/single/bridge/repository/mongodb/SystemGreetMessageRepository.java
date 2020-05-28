package com.gaea.single.bridge.repository.mongodb;

import com.gaea.single.bridge.entity.mongodb.SystemGreetMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/** @author cludy */
public interface SystemGreetMessageRepository
    extends ReactiveMongoRepository<SystemGreetMessage, Long> {}
