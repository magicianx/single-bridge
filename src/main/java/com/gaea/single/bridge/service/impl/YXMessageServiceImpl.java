package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.DefaultSettingConstant;
import com.gaea.single.bridge.constant.YxCcidConstant;
import com.gaea.single.bridge.core.manager.MessageManager;
import com.gaea.single.bridge.core.yx.YxClient;
import com.gaea.single.bridge.entity.mysql.UserRegInfo;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.repository.mysql.UserRegInfoRepository;
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
  @Autowired private MessageManager messageCache;
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
    String appId = DefaultSettingConstant.APP_ID;

    if (UserType.ANCHOR == userType) {
      return userRegInfoRepository
          .listAuditPassedInfos(osType.getCode(), appId, 0, 500)
          .collectList()
          .flatMap(
              users -> {
                List<String> yunXinIds =
                    users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                return yxClient.sendMessage(
                    YxCcidConstant.ANCHOR_SECRETARY_CCID, null, yunXinIds, content);
              });
    } else {
      Mono<Void> mono =
          userRegInfoRepository
              .listUnAuditPassedInfos(osType.getCode(), appId, 0, 100)
              .collectList()
              .flatMap(
                  users -> {
                    List<String> yunXinIds =
                        users.stream().map(UserRegInfo::getYunxinId).collect(Collectors.toList());
                    return yxClient.sendMessage(
                        YxCcidConstant.USER_SECRETARY_CCID, null, yunXinIds, content);
                  });
      for (int i = 1; i < 33; i++) {
        mono =
            mono.then(
                userRegInfoRepository
                    .listUnAuditPassedInfos(osType.getCode(), appId, i * 100, 100)
                    .collectList()
                    .flatMap(
                        users -> {
                          List<String> yunXinIds =
                              users.stream()
                                  .map(UserRegInfo::getYunxinId)
                                  .collect(Collectors.toList());
                          return yxClient.sendMessage(
                              YxCcidConstant.USER_SECRETARY_CCID, null, yunXinIds, content);
                        }));
      }
      return mono;
    }
  }
}
