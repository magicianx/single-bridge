package com.gaea.single.bridge.service;

import com.gaea.single.bridge.dto.gratuity.GratuityGiftRes;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 打赏服务
 *
 * @author cludy
 */
public interface GratuityService {
  /**
   * 获取主播被打赏礼物列表
   *
   * @param userId 用户id
   * @return {@link Mono<List<GratuityGiftRes>>}
   */
  Mono<List<GratuityGiftRes>> getGratuityGifts(Long userId);
}
