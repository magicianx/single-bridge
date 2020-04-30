package com.gaea.single.bridge.service;

import com.gaea.single.bridge.entity.UserSocialInfo;
import reactor.core.publisher.Mono;

public interface UserSocialInfoService {
  Mono<UserSocialInfo> findByShowId(Long currentUserId, String showId);
}
