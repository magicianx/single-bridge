package com.gaea.single.bridge.core.cache;

import com.gaea.single.bridge.constant.CacheConstant;
import com.gaea.single.bridge.enums.BoolType;
import com.gaea.single.bridge.repository.mongodb.UserRepository;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * 用户缓存
 *
 * @author cludy
 */
@Component
public class UserCache extends AbstractCache {
  @Autowired private UserRepository userRepository;

  /**
   * 获取用户开启定位状态
   *
   * @param userId ¬用户id
   */
  public Mono<Boolean> getPositionStatus(Long userId) {
    String key = getKey(CacheConstant.USER_POSITION_STATUS, userId);
    RBucketReactive<Long> bucket = redission.getBucket(key, LongCodec.INSTANCE);

    return bucket
        .get()
        .map(v -> BoolType.ofCode(v.intValue()).getValue())
        .switchIfEmpty(
            Mono.defer(
                () ->
                    userRepository
                        .findById(userId)
                        .flatMap(
                            user ->
                                bucket
                                    .set(
                                        BoolType.ofValue(user.getIsEnablePosition()).getCode(),
                                        300,
                                        TimeUnit.SECONDS)
                                    .thenReturn(user.getIsEnablePosition()))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(true)))));
  }

  /**
   * 设置用户开启定位状态
   *
   * @param userId 用户id
   * @param isEnable 是否开启定位
   */
  public Mono<Void> setPositionStatus(Long userId, boolean isEnable) {
    String key = getKey(CacheConstant.USER_POSITION_STATUS, userId);
    RBucketReactive<Long> bucket = redission.getBucket(key, LongCodec.INSTANCE);
    return userRepository
        .findById(userId)
        .flatMap(
            user -> {
              user.setIsEnablePosition(isEnable);
              return userRepository.save(user);
            })
        .then(bucket.delete().then(Mono.empty()));
  }
}
