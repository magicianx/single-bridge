package com.gaea.single.bridge.core.manager;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.DefaultSettingConstant;
import com.gaea.single.bridge.constant.SingleRedisConstant;
import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.enums.UserRealOnlineStatus;
import com.gaea.single.bridge.repository.mysql.UserRegInfoRepository;
import com.gaea.single.bridge.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

/**
 * 打招呼用户管理
 *
 * @author cludy
 */
@Component
@Slf4j
@DependsOn("configAgent")
public class GreetUserManager extends AbstractCache {
  @Autowired private UserRegInfoRepository userRegInfoRepository;
  @Autowired private UserManager userManager;

  private RScoredSortedSetReactive<Long> newUserSet;
  private RScoredSortedSetReactive<Long> uncalledUserSet;
  private RQueueReactive<Long> greetUsingQueue;

  @PostConstruct
  public void init() {
    this.newUserSet = singleRedission.getScoredSortedSet(key(SingleRedisConstant.USER_GREET_NEW));
    this.uncalledUserSet =
        singleRedission.getScoredSortedSet(key(SingleRedisConstant.USER_GREET_UNCALLED));
    this.greetUsingQueue = singleRedission.getQueue(key(SingleRedisConstant.USER_GREET_USING));
    initGreetUser().subscribe();
  }

  /**
   * 添加到打招呼队列（新用户队列和未呼叫队列）
   *
   * @param userId 用户id
   * @param isNewUser 是否为新用户
   * @return {@link Mono <Void>}
   */
  public Mono<Void> addGreetUser(Long userId, boolean isNewUser) {
    return singleRedission
        .getAtomicLong(key(SingleRedisConstant.USER_GREET_TIMES, userId))
        .get()
        .flatMap(
            v -> {
              if (isReachedMaxReceiveTimes(v)) {
                log.info("用户{}已达到最大接收打招呼次数,不添加到队列", userId);
                return Mono.empty();
              }
              if (!isNewUser) {
                return newUserSet
                    .contains(userId)
                    .flatMap(
                        exist -> {
                          // 新用户队列中不存在则加入到未打呼叫用户队列
                          if (!exist) {
                            log.info("已添加用户{}到未到打招呼队列", userId);
                            return uncalledUserSet
                                // 时间戳作为score
                                .add(System.currentTimeMillis(), userId)
                                .then();
                          }
                          return Mono.empty();
                        });
              }
              log.info("已添加用户{}到新用户打招呼队列", userId);
              return newUserSet.add(System.currentTimeMillis(), userId).then();
            });
  }

  public Mono<Void> removeOverDayNewUser() {
    log.info("从新用户队列中移除注册超过{}天的用户", DictionaryProperties.get().getGreetMessage().getNewUserDays());
    long endScore =
        LocalDate.now()
            .minusDays(7)
            .atStartOfDay(ZoneId.of("Asia/Shanghai"))
            .toInstant()
            .toEpochMilli();
    // 移除7天前的用户
    return newUserSet.removeRangeByScore(0, true, endScore, false).then();
  }

  public Mono<Void> removeUncalledUser(Long userId) {
    log.info("从未呼叫队列中移除用户{}", userId);
    return uncalledUserSet.remove(userId).then();
  }

  /**
   * 移除用户
   *
   * @param userId 用户id
   * @return {@link Mono<Void>}
   */
  public Mono<Void> removeGreetUser(Long userId) {
    log.info("将用户{}从新用户和未呼叫队列中移除", userId);
    return uncalledUserSet.remove(userId).then(Mono.defer(() -> newUserSet.remove(userId).then()));
  }

  /**
   * 获取指定数量的打招呼用户id
   *
   * @param count 用户数量, 必须为偶数
   * @return {@link Mono<Set<Long>>}
   */
  public Mono<Set<Long>> getGreetUsers(Long currentUserId, int count) {
    Set<Long> greetUsers = new HashSet<>();
    int maxReceiveTimes = DictionaryProperties.get().getGreetMessage().getMaxReceiveGreetTimes();

    Mono<Void> addNewUsers =
        newUserSet
            .readAll()
            .flatMap(
                v -> {
                  log.info("已重新发放入{}个新用户到使用队列中", v.size());
                  return greetUsingQueue.addAll(v).then();
                })
            .switchIfEmpty(
                Mono.defer(
                    () -> {
                      log.info("无新用户放入打招呼使用队列");
                      return Mono.empty();
                    }));

    Mono<Void> addUncalledUsers =
        uncalledUserSet
            .size()
            .flatMap(
                size -> {
                  if (size > 0) {
                    log.info("已重新发放入{}个未呼叫用户到使用队列中", size);
                    // 分数为时间未呼叫时间查询
                    long endScore =
                        System.currentTimeMillis()
                            - DictionaryProperties.get().getGreetMessage().getUncalledUserSecond()
                                * 1000;
                    return uncalledUserSet
                        .valueRange(0, true, endScore, false)
                        .flatMap(v -> greetUsingQueue.addAll(v))
                        .then();
                  } else {
                    log.info("无未呼叫用户放入打招呼使用队列");
                    return Mono.empty();
                  }
                });
    Mono<Set<Long>> rePutToUsingQueue =
        Mono.when(addNewUsers, addUncalledUsers)
            .then(
                Mono.defer(
                    () ->
                        popGreetUser(currentUserId, count, maxReceiveTimes, greetUsers)
                            .map(
                                users -> {
                                  log.info("用户{}可发送打招呼消息给{}个用户", currentUserId, users.size());
                                  return users;
                                })
                            .switchIfEmpty(
                                Mono.defer(
                                    () -> {
                                      log.info("无可发送用户,用户{}不发送打招呼消息", currentUserId);
                                      return Mono.just(Collections.emptySet());
                                    }))));

    return popGreetUser(currentUserId, count, maxReceiveTimes, greetUsers)
        .flatMap(
            users -> {
              // 可发送用户数量小于需要数量, 从原始set中放入使用set中
              if (users.size() < count) {
                log.info("获得可打招呼用户数量{}小于需要数量{}, 从新放入可打招呼用户到使用队列", users.size(), count);
                return rePutToUsingQueue;
              } else {
                log.info("用户{}可发送打招呼消息给{}个用户", currentUserId, users.size());
                return Mono.just(users);
              }
            })
        .switchIfEmpty(Mono.defer(() -> rePutToUsingQueue));
  }

  private Mono<Set<Long>> popGreetUser(
      Long currentUserId, int count, int maxReceiveTimes, Set<Long> greetUsers) {
    Function<Long, Mono<Set<Long>>> removeUserAction =
        (userId) ->
            uncalledUserSet
                .remove(userId)
                .flatMap((v) -> popGreetUser(currentUserId, count, maxReceiveTimes, greetUsers));

    return greetUsingQueue
        .poll()
        .flatMap(
            userId ->
                userManager
                    .getUserRealOnlineStatus(userId)
                    .flatMap(
                        status -> {
                          if (!userId.equals(currentUserId)) {
                            if (status == UserRealOnlineStatus.ONLINE) {
                              return userManager
                                  .getUserGender(userId)
                                  .flatMap(
                                      genderType -> {
                                        if (GenderType.MALE.equals(genderType)) {
                                          log.debug("用户{}加入到被呼叫列表中", userId);
                                          return increaseReceiveTimes(userId, maxReceiveTimes)
                                              .flatMap(
                                                  success -> {
                                                    if (success) {
                                                      greetUsers.add(userId);
                                                    }
                                                    return greetUsers.size() == count
                                                        ? Mono.just(greetUsers)
                                                        : popGreetUser(
                                                            currentUserId,
                                                            count,
                                                            maxReceiveTimes,
                                                            greetUsers);
                                                  });
                                        } else {
                                          log.info("用户{}为女性，从未呼叫队列中移除", userId);
                                          return removeUserAction.apply(userId);
                                        }
                                      });
                            } else {
                              log.debug("用户{}不在线，从未呼叫队列中移除", userId);
                              return removeUserAction.apply(userId);
                            }
                          }
                          return popGreetUser(currentUserId, count, maxReceiveTimes, greetUsers);
                        })
                    .switchIfEmpty(
                        Mono.defer(
                            () -> {
                              log.debug("用户{}在线状态未知，从未呼叫队列中移除", userId);
                              return removeUserAction.apply(userId);
                            })))
        // 使用中的列表已经弹完了
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  log.debug("共可发送打招呼消息给{}个用户", greetUsers.size());
                  return Mono.just(greetUsers);
                }));
  }

  private Mono<Boolean> increaseReceiveTimes(Long userId, int maxReceiveTimes) {
    RAtomicLongReactive greetAtomicTimes =
        singleRedission.getAtomicLong(key(SingleRedisConstant.USER_GREET_TIMES, userId));
    return greetAtomicTimes
        .incrementAndGet()
        .flatMap(
            v -> {
              if (v == 1) {
                return greetAtomicTimes.expireAt(getExpireDate()).map(s -> true);
              } else {
                return removeReachMaxReceivedUser(userId, v, maxReceiveTimes)
                    .map(removed -> !removed);
              }
            });
  }

  private Mono<Boolean> removeReachMaxReceivedUser(
      Long userId, long receiveTimes, int maxReceiveTimes) {
    // 如果次数达到最大接收次数，未呼叫队列中移除
    if (receiveTimes >= maxReceiveTimes) {
      log.info("用户{}已达到最大接收招呼次数{}", userId, maxReceiveTimes);
      return uncalledUserSet.remove(userId);
    }
    return Mono.just(false);
  }

  private Date getExpireDate() {
    LocalDate now = LocalDate.now().plusDays(1);
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
    calendar.set(now.getYear(), now.getMonthValue(), now.getDayOfYear());
    return calendar.getTime();
  }

  /**
   * 初始化可打招呼新用户队列
   *
   * @return {@link Mono<Void>}
   */
  private Mono<Void> initGreetUser() {
    log.info("正在检查可打招呼新用户队列是否需要初始化");
    LocalDate endDate = LocalDate.now();
    LocalDate startDate =
        endDate.minusDays(DictionaryProperties.get().getGreetMessage().getNewUserDays() - 1);

    return newUserSet
        .size()
        .flatMap(
            size -> {
              if (size == 0) {
                log.info("可打招呼新用户列队为空, 正在初始化");
                return userRegInfoRepository
                    .findNewRegisterUser(
                        DefaultSettingConstant.APP_ID,
                        DateUtil.toDbDate(startDate),
                        DateUtil.toDbDate(endDate))
                    .flatMap(
                        user ->
                            newUserSet
                                .add(DateUtil.getMilliseconds(user.getCreateTime()), user.getId())
                                .flatMap(
                                    v -> {
                                      if (v) {
                                        return greetUsingQueue.delete().then();
                                      }
                                      return Mono.empty();
                                    }))
                    .then(Mono.empty());
              } else {
                log.info("可打招呼新用户队列不为空, 无需初始化");
                return Mono.empty();
              }
            });
  }

  private boolean isReachedMaxReceiveTimes(long currentReceiveTimes) {
    return currentReceiveTimes
        >= DictionaryProperties.get().getGreetMessage().getMaxReceiveGreetTimes();
  }
}
