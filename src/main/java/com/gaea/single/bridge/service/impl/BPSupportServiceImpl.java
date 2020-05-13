package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.service.BPSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BPSupportServiceImpl implements BPSupportService {
  @Autowired private DatabaseClient client;

  @Override
  public Mono<Boolean> isFirstRecharge(Long userId) {
    return client
        .execute("select count(*) from order_recharge_detail where user_id = ? and status = 2")
        .bind(0, userId)
        .as(Integer.class)
        .fetch()
        .one()
        .map(count -> count == 1);
  }

  @Override
  public Mono<Boolean> isTodayRegisterAndFirstRecharge(Long userId) {
    return client
        .execute(
            "select count(*)\n"
                + "from user_reg_info u\n"
                + "         left join order_recharge_detail o on u.id = o.user_id\n"
                + "where date(u.create_time) = CURDATE()\n"
                + "  and u.id = ?\n"
                + "  and o.status = 2")
        .bind(0, userId)
        .as(Integer.class)
        .fetch()
        .one()
        .map(count -> count <= 1);
  }

  @Override
  public Mono<Boolean> isFirstOpenVip(Long userId) {
    return client
        .execute("select count(*) from order_buy_privilege where status = 2 and user_id = ?")
        .bind(0, userId)
        .as(Integer.class)
        .fetch()
        .one()
        .map(count -> count <= 1);
  }

  public Mono<Boolean> isInRegisterChannel(Long userId, String channel) {
    return client
        .execute("select count(*) from user_reg_info where channel = ? and id = ?")
        .bind(0, channel)
        .bind(1, userId)
        .as(Integer.class)
        .fetch()
        .one()
        .map(count -> count <= 1);
  }
}
