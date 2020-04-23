package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.entity.UserSocialInfo;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.repository.UserSocialInfoRepository;
import com.gaea.single.bridge.service.UserSocialInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRegInfoServiceImpl implements UserSocialInfoService {

  @Autowired private UserSocialInfoRepository userRegInfoRepository;

  @Override
  public Mono<UserSocialInfo> findByShowId(Long currentUserId, String showId) {
    return userRegInfoRepository
        .findById(currentUserId)
        .map(
            user -> {
              // 当前用户是否为主播
              return AuditStatus.ofCode(user.getIsVideoAudit()) == AuditStatus.PASS;
            })
        .flatMap(
            isAnchor ->
                isAnchor
                    ? userRegInfoRepository.findAnchorByShowId(showId)
                    : userRegInfoRepository.findGeneralUserByShowId(showId));
  }
}
