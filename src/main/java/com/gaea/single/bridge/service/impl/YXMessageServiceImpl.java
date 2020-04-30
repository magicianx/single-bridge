package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.core.cache.MessageCache;
import com.gaea.single.bridge.core.yx.YxClient;
import com.gaea.single.bridge.entity.UserRegInfo;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.repository.UserRegInfoRepository;
import com.gaea.single.bridge.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class YXMessageServiceImpl implements MessageService {
  @Autowired private MessageCache messageCache;
  @Autowired private YxClient yxClient;
  @Autowired private UserRegInfoRepository userRegInfoRepository;

  @Override
  public Mono<Integer> getMessageCount(Long userId) {
    Integer defaultCount = DictionaryProperties.get().getUser().getDefaultMessageCount();
    return messageCache.getMessageCount(userId, defaultCount);
  }

  @Override
  public Mono<Integer> decrMessageCount(Long userId) {
    return messageCache.decrMessageCount(userId);
  }

  @Override
  public Mono<Void> batchSendTextMsg(OsType osType, String content) {
    String appId = DictionaryProperties.get().getLobo().getAppId();
    Mono<Void> anchorMono =
        userRegInfoRepository
            .listAuditPassedYunXinIds(osType.getCode(), appId)
            .collectList()
            .flatMap(
                users -> {
                  List<String> yunXinIds =
                      users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                  return yxClient.sendBatchTextMsg(UserType.ANCHOR, yunXinIds, content);
                });

    Mono<Void> userMono =
        userRegInfoRepository
            .listUnAuditPassedYunXinIds(osType.getCode(), appId)
            .collectList()
            .flatMap(
                users -> {
                  List<String> yunXinIds =
                      users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                  return yxClient.sendBatchTextMsg(UserType.GENERAL_USER, yunXinIds, content);
                });
    return Mono.zip(anchorMono, userMono, (t1, t2) -> t1);
  }
}
