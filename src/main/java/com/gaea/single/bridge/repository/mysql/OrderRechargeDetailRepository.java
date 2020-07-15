package com.gaea.single.bridge.repository.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * 订单充值详情
 *
 * @author JunJYu
 * @date 2020/7/9 16:31
 */
@Repository
public class OrderRechargeDetailRepository {

  @Autowired private DatabaseClient databaseClient;

  /**
   * 获取用户充值次数
   *
   * @param userId 用户id
   * @return {@link Mono<Integer>}
   */
  public Mono<Integer> getRechargeTimes(Long userId) {
    return databaseClient
        .execute("select count(*) from order_recharge_detail " + "where user_id = ? and status = 2")
        .bind(0, userId)
        .as(Integer.class)
        .fetch()
        .one();
  }
}
