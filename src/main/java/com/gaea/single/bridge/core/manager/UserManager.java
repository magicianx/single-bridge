package com.gaea.single.bridge.core.manager;

import com.gaea.single.bridge.constant.LoboRedisConstant;
import com.gaea.single.bridge.entity.mongodb.User;
import com.gaea.single.bridge.entity.mysql.UserPersonalInfo;
import com.gaea.single.bridge.enums.BoolType;
import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.enums.UserRealOnlineStatus;
import com.gaea.single.bridge.repository.mongodb.UserRepository;
import com.gaea.single.bridge.repository.mysql.UserPersonalInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
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
@Slf4j
public class UserManager extends AbstractCache {
  @Autowired private UserRepository userRepository;
  @Autowired private UserPersonalInfoRepository userPersonalInfoRepository;

  /**
   * 获取用户性别
   *
   * @param userId 用户id
   * @return {@link RMapReactive<String, String>}
   */
  public Mono<GenderType> getUserGender(Long userId) {
    return loboRedission
        .getMap(LoboRedisConstant.USER_INFO + userId, StringCodec.INSTANCE)
        .get("sex")
        .switchIfEmpty(
            Mono.defer(
                () -> userPersonalInfoRepository.findById(userId).map(UserPersonalInfo::getSex)))
        .flatMap(
            t -> {
              String type = (String) t;
              return StringUtils.isNotBlank(type)
                  ? Mono.just(GenderType.ofCode(Integer.parseInt(type)))
                  : Mono.empty();
            });
  }

  /**
   * 获取用户信息
   *
   * @param userId 用户id
   * @return
   */
  public Mono<User> getUser(Long userId) {
    return userRepository
        .findById(userId)
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  log.info("正在创建用户初始化信息: " + userId);
                  return userRepository.save(new User(userId, true, true));
                }));
  }

  /**
   * 获取用户在线状态
   *
   * @param userId 用户id
   * @return {@link Mono<UserOnlineStatus> }
   */
  public Mono<UserOnlineStatus> getUserOnlineStatus(Long userId) {
    return loboRedission
        .getSet(LoboRedisConstant.USER_UN_DISTURB)
        .contains(userId)
        .flatMap(
            dnd -> {
              if (dnd) {
                return Mono.just(UserOnlineStatus.UN_DISTURB);
              }
              return loboRedission
                  .getSet(LoboRedisConstant.USER_FREE, StringCodec.INSTANCE)
                  .contains(userId)
                  .flatMap(
                      free -> {
                        if (free) {
                          return Mono.just(UserOnlineStatus.FREE);
                        }
                        return loboRedission
                            .getSet(LoboRedisConstant.USER_BUSY)
                            .contains(userId)
                            .flatMap(
                                busy -> {
                                  if (busy) {
                                    return Mono.just(UserOnlineStatus.CHATTING);
                                  }
                                  return Mono.just(UserOnlineStatus.UN_DISTURB);
                                });
                      });
            });
  }

  /**
   * 获取用户的真实在线状态
   *
   * @return {@link Mono<UserRealOnlineStatus>}
   */
  public Mono<UserRealOnlineStatus> getUserRealOnlineStatus(Long userId) {
    return loboRedission
        .getBucket(LoboRedisConstant.USER_REAL_STATUS + userId, StringCodec.INSTANCE)
        .get()
        .map(index -> UserRealOnlineStatus.ofIndex(Integer.valueOf(index.toString())).orElse(null));
  }

  /**
   * 获取用户开启定位状态
   *
   * @param userId ¬用户id
   */
  public Mono<Boolean> getPositionStatus(Long userId) {
    String key = key(LoboRedisConstant.USER_POSITION_STATUS, userId);
    RBucketReactive<Long> bucket = singleRedission.getBucket(key, LongCodec.INSTANCE);

    return bucket
        .get()
        .map(v -> BoolType.ofCode(v.intValue()).getValue())
        .switchIfEmpty(
            Mono.defer(
                () ->
                    getUser(userId)
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
    String key = key(LoboRedisConstant.USER_POSITION_STATUS, userId);
    RBucketReactive<Long> bucket = singleRedission.getBucket(key, LongCodec.INSTANCE);
    return getUser(userId)
        .flatMap(
            user -> {
              user.setIsEnablePosition(isEnable);
              return userRepository.save(user);
            })
        .then(bucket.delete().then(Mono.empty()));
  }

  /**
   * 用户是否为vip
   *
   * @param userId 用户id
   * @return {@link Mono<Boolean>}
   */
  public Mono<Boolean> isVip(Long userId) {
    return loboRedission
        .getMap(key(LoboRedisConstant.USER_VIP_INFO, userId), StringCodec.INSTANCE)
        .isExists();
  }
}
