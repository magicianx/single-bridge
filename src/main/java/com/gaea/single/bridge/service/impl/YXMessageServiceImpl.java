package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.core.cache.MessageCache;
import com.gaea.single.bridge.core.yx.YxClient;
import com.gaea.single.bridge.entity.UserRegInfo;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.repository.UserRegInfoRepository;
import com.gaea.single.bridge.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
  public Mono<Void> batchSendTextMsg(OsType osType, UserType userType, String content) {
    log.info("正在批量推送文本消息: osType {}, content {}", osType.name(), content);
    String appId = DictionaryProperties.get().getLobo().getAppId();

    if (UserType.ANCHOR == userType) {

      return userRegInfoRepository
          .listAuditPassedYunXinIds(osType.getCode(), appId, 0, 500)
          .collectList()
          .flatMap(
              users -> {
                List<String> yunXinIds =
                    users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                return yxClient.sendBatchTextMsg(userType, yunXinIds, content);
              });
    } else {
      Mono<Void> mono =
          userRegInfoRepository
              .listUnAuditPassedYunXinIds(osType.getCode(), appId, 0, 100)
              .collectList()
              .flatMap(
                  users -> {
                    List<String> yunXinIds =
                        users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                    return yxClient.sendBatchTextMsg(userType, yunXinIds, content);
                  });
      for (int i = 1; i < 33; i++) {
        mono =
            mono.then(
                userRegInfoRepository
                    .listUnAuditPassedYunXinIds(osType.getCode(), appId, i * 100, 100)
                    .collectList()
                    .flatMap(
                        users -> {
                          List<String> yunXinIds =
                              users.stream()
                                  .map(UserRegInfo::getYunxinId)
                                  .collect(Collectors.toList());
                          return yxClient.sendBatchTextMsg(userType, yunXinIds, content);
                        }));
      }
      return mono;
    }
  }
}
