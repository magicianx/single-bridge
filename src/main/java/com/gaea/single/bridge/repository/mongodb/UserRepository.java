package com.gaea.single.bridge.repository.mongodb;

import com.gaea.single.bridge.entity.mongodb.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * User Repository
 *
 * @author cludy
 */
public interface UserRepository extends ReactiveMongoRepository<User, Long> {}
