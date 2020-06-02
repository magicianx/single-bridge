package com.gaea.single.bridge.core.manager;

import com.gaea.single.bridge.constant.RedisConstant;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.LongCodec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** 云信消息管理 */
@Component
public class MessageManager extends AbstractCache {
  /**
   * 获取用户剩余发送云信消息数量，如果不存在则设置默值, 如果发现值小于0，会将值重置为0
   *
   * @param userId 用户id
   * @return 剩余消息数量
   */
  public Mono<Integer> getMessageCount(Long userId, int count) {
    String key = key(RedisConstant.USER_MESSAGE_COUNT, userId);
    RBucketReactive<Long> bucket = redission.getBucket(key, LongCodec.INSTANCE);
    return bucket
        .trySet((long) count)
        .flatMap(
            success ->
                bucket
                    .get()
                    .flatMap(
                        v -> {
                          // 如果小于0， 则重置为0
                          if (v < 0) {
                            return bucket.set(0L).flatMap(vd -> Mono.just(0));
                          }
                          return Mono.just(v.intValue());
                        }));
  }

  /**
   * 将用户剩余发送云信消息数量减1, 这里没有使用并发锁, 所以恶意操作可能造成小于0, 但是没有关系，我们在{@link
   * MessageManager#getMessageCount}会将它重置为0
   *
   * @param userId 用户id
   * @return 剩余消息数量
   */
  public Mono<Integer> decrMessageCount(Long userId) {
    String key = key(RedisConstant.USER_MESSAGE_COUNT, userId);
    RAtomicLongReactive value = redission.getAtomicLong(key);

    return value
        .get()
        .flatMap(
            v -> {
              if (v > 0) {
                return value.decrementAndGet().map(Long::intValue);
              }
              return Mono.just(v.intValue());
            });
  }
}
