package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.entity.UserSocialInfo;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.repository.UserSocialInfoRepository;
import com.gaea.single.bridge.service.UserSocialInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRegInfoServiceImpl implements UserSocialInfoService {

  @Autowired private UserSocialInfoRepository userSocialInfoRepository;

  @Override
  public Mono<UserSocialInfo> findByShowId(Long currentUserId, String showId) {
    // 用户未登录，搜索主播
    if (currentUserId == null) {
      return userSocialInfoRepository.findAnchorByShowId(
          showId, DictionaryProperties.get().getLobo().getSystemLabelId());
    }
    return userSocialInfoRepository
        .findById(currentUserId)
        .map(
            user -> {
              // 当前用户是否为主播
              return AuditStatus.ofCode(user.getIsVideoAudit()) == AuditStatus.PASS;
            })
        .flatMap(
            isAnchor ->
                isAnchor
                    ? userSocialInfoRepository.findGeneralUserByShowId(showId)
                    : userSocialInfoRepository.findAnchorByShowId(
                        showId, DictionaryProperties.get().getLobo().getSystemLabelId()));
  }
}
