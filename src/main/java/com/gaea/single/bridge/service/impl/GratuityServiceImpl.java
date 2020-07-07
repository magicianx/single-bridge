package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.dto.gratuity.GratuityGiftRes;
import com.gaea.single.bridge.repository.mysql.GratuityDetailDao;
import com.gaea.single.bridge.service.GratuityService;
import com.gaea.single.bridge.util.LoboUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/** @author cludy */
@Service
@Slf4j
public class GratuityServiceImpl implements GratuityService {
  @Autowired private GratuityDetailDao gratuityDetailDao;

  @Override
  public Mono<List<GratuityGiftRes>> getGratuityGifts(Long userId) {
    return gratuityDetailDao
        .statisticGratuityGiftCounts(userId)
        .map(
            item ->
                new GratuityGiftRes(
                    item.getGiftId(),
                    item.getGiftName(),
                    LoboUtil.getImageUrl(item.getGiftIconUrl()),
                    item.getGiftNum()))
        .collectList();
  }
}
