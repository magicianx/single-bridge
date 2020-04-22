package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.core.cache.MessageCache;
import com.gaea.single.bridge.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class YXMessageServiceImpl implements MessageService {
  @Autowired private MessageCache messageCache;

  @Override
  public Mono<Integer> getMessageCount(Long userId) {
    Integer defaultCount = DictionaryProperties.get().getUser().getDefaultMessageCount();
    return messageCache.getMessageCount(userId, defaultCount);
  }

  @Override
  public Mono<Void> decrMessageCount(Long userId) {
    return messageCache.decrMessageCount(userId);
  }
}
