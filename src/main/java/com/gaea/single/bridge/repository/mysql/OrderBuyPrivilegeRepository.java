package com.gaea.single.bridge.repository.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * 特权购买订单
 *
 * @author JunJYu
 * @date 2020/7/9 16:04
 */
@Repository
public class OrderBuyPrivilegeRepository {

  @Autowired private DatabaseClient databaseClient;

  public Mono<Integer> buyCount(Long userId) {
    return databaseClient
        .execute("select count(*) from order_buy_privilege "
                + "where user_id = ? and status = 2")
        .bind(0, userId)
        .as(Integer.class)
        .fetch()
        .one();
  }
}
