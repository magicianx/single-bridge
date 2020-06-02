package com.gaea.single.bridge.core.manager;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.DefaultSettingConstant;
import com.gaea.single.bridge.constant.RedisConstant;
import com.gaea.single.bridge.core.manager.model.GreetUser;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.repository.mysql.UserRegInfoRepository;
import com.gaea.single.bridge.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 打招呼用户管理
 *
 * @author cludy
 */
@Component
@Slf4j
public class GreetUserManager extends AbstractCache {
  @Autowired private UserRegInfoRepository userRegInfoRepository;
  @Autowired private UserManager userManager;

  private RScoredSortedSetReactive<GreetUser> newUserSet;
  private RScoredSortedSetReactive<GreetUser> uncalledUserSet;
  private RQueueReactive<GreetUser> greetUsingQueue;

  @PostConstruct
  public void init() {
    this.newUserSet = singleRedission.getScoredSortedSet(key(RedisConstant.USER_GREET_NEW));
    this.uncalledUserSet = singleRedission.getScoredSortedSet(key(RedisConstant.USER_GREET_UNCALLED));
    this.greetUsingQueue = singleRedission.getQueue(key(RedisConstant.USER_GREET_USING));
    initGreetUser().subscribe();
  }

  /**
   * 添加到打招呼队列（新用户队列和未呼叫队列）
   *
   * @param greetUser 打招呼用户
   * @param isNewUser 是否为新用户
   * @return {@link Mono <Void>}
   */
  public Mono<Void> addGreetUser(GreetUser greetUser, boolean isNewUser) {
    long userId = greetUser.getUserId();
    return singleRedission
        .getAtomicLong(key(RedisConstant.USER_GREET_TIMES, userId))
        .get()
        .flatMap(
            v -> {
              if (isReachedMaxReceiveTimes(v)) {
                log.info("用户{}已达到最大接收打招呼次数,不添加到队列", userId);
                return Mono.empty();
              }
              GreetUser newUser = new GreetUser(userId, greetUser.getYunxinId());
              if (!isNewUser) {
                return newUserSet
                    .contains(newUser)
                    .flatMap(
                        exist -> {
                          // 新用户队列中不存在加入到未呼叫用户队列
                          if (!exist) {
                            long createTime = System.currentTimeMillis();
                            log.info("已添加用户{}未呼叫用户打招呼队列", userId);
                            return uncalledUserSet
                                // 时间戳作为score
                                .add(createTime, newUser)
                                .then();
                          }
                          return Mono.empty();
                        });
              }
              log.info("已添加用户{}新用户打招呼队列", userId);
              return newUserSet.add(0, new GreetUser(userId, greetUser.getYunxinId())).then();
            });
  }

  public Mono<Void> removeOverDayNewUser() {
    log.info("从新用户队列中移除注册超过{}天的用户", DictionaryProperties.get().getGreetMessage().getNewUserDays());
    // 移除7天前的用户
    return newUserSet.removeRangeByScore(0, true, 0, true).then();
  }

  public Mono<Void> removeUncalledUser(Long userId) {
    return userRegInfoRepository
        .findById(userId)
        .flatMap(
            u -> {
              log.info("从未呼叫用户队列中删除用户{}", u.getId());
              return uncalledUserSet.remove(new GreetUser(u.getId(), u.getYunxinId())).then();
            });
  }

  /**
   * 获取指定数量的打招呼用户
   *
   * @param count 用户数量, 必须为偶数
   * @return {@link Mono<Collection<GreetUser>>}
   */
  public Mono<? extends Collection<GreetUser>> getGreetUsers(Long currentUserId, int count) {
    Set<GreetUser> greetUsers = new HashSet<>();
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
                    // 结束分数为当前时间戳减去5秒
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
    Mono<Set<GreetUser>> rePutToUsingQueue =
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

  private Mono<Set<GreetUser>> popGreetUser(
      Long currentUserId, int count, int maxReceiveTimes, Set<GreetUser> greetUsers) {
    return greetUsingQueue
        .poll()
        .flatMap(
            user -> {
              if (user != null) {
                return userManager
                    .getUserOnlineStatus(user.getUserId())
                    .flatMap(
                        status -> {
                          if (user.getUserId() != currentUserId) {
                            if (status == UserOnlineStatus.FREE) {
                              greetUsers.add(user);
                              return increaseReceiveTimes(user, maxReceiveTimes)
                                  .flatMap(
                                      success ->
                                          greetUsers.size() == count
                                              ? Mono.just(greetUsers)
                                              : popGreetUser(
                                                  currentUserId,
                                                  count,
                                                  maxReceiveTimes,
                                                  greetUsers));
                            } else {
                              uncalledUserSet.remove(user);
                            }
                          }

                          return popGreetUser(currentUserId, count, maxReceiveTimes, greetUsers);
                        });
                // 使用中的列表已经弹完了
              } else {
                return Mono.just(greetUsers);
              }
            });
  }

  private Mono<Boolean> increaseReceiveTimes(GreetUser user, int maxReceiveTimes) {
    RAtomicLongReactive greetAtomicTimes =
        singleRedission.getAtomicLong(key(RedisConstant.USER_GREET_TIMES, user.getUserId()));
    return greetAtomicTimes
        .incrementAndGet()
        .flatMap(
            v -> {
              if (v == 1) {
                return greetAtomicTimes
                    .expireAt(getExpireDate())
                    .flatMap(s -> removeUncalledUser(user, v, maxReceiveTimes));
              } else {
                return removeUncalledUser(user, v, maxReceiveTimes);
              }
            });
  }

  private Mono<Boolean> removeUncalledUser(GreetUser user, long receiveTimes, int maxReceiveTimes) {
    // 如果次数达到最大接收次数，从新用户和未呼叫队列中移除
    if (receiveTimes >= maxReceiveTimes) {
      log.info("用户{}已达到最大接收招呼次数{}", user.getUserId(), maxReceiveTimes);
      return newUserSet.remove(user).then(Mono.defer(() -> uncalledUserSet.remove(user)));
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
                        user -> {
                          int score =
                              (int) (ChronoUnit.DAYS.between(endDate, user.getCreateTime()) + 1);
                          GreetUser greetUser = new GreetUser(user.getId(), user.getYunxinId());
                          return newUserSet
                              .add(score, greetUser)
                              .flatMap(
                                  v -> {
                                    if (v) {
                                      return greetUsingQueue.delete().then();
                                    }
                                    return Mono.empty();
                                  });
                        })
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
