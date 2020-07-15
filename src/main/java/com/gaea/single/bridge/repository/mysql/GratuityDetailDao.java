package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.model.GratuityGiftCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/** @author cludy */
@Component
public class GratuityDetailDao {
  @Autowired private DatabaseClient databaseClient;

  /**
   * 统计主播被打赏礼物数量
   *
   * @param userId 主播用户id
   * @return {@link Flux<GratuityGiftCount>}
   */
  public Flux<GratuityGiftCount> statisticGratuityGiftCounts(Long userId) {
    return databaseClient
        .execute(
            "select count(*) gift_num, gratuity_config_id gift_id, c.picture_name gift_name, c.picture_url gift_icon_url\n"
                + "from gratuity_detail g\n"
                + "         join order_gratuity_detail o on g.id = o.gratuity_detail_id\n"
                + "         join boss.base_gratuity_config c on g.gratuity_config_id = c.id\n"
                + "where g.to_user_id = :userId\n"
                + "  and (o.status = 2 or o.status = 3)\n"
                + "group by g.gratuity_config_id\n"
                + "order by c.money desc")
        .bind("userId", userId)
        .as(GratuityGiftCount.class)
        .fetch()
        .all();
  }

  /**
   * 查询最近礼物列表, 价格由礼物单价从高到低排列
   *
   * @param userId 主播用户id
   * @return {@link Flux<String>}
   */
  public Flux<String> findRecentGifts(Long userId) {
    return databaseClient
        .execute(
            "select c.picture_url gift_icon_url\n"
                + "from gratuity_detail g\n"
                + "         join order_gratuity_detail o on g.id = o.gratuity_detail_id\n"
                + "         join boss.base_gratuity_config c on g.gratuity_config_id = c.id\n"
                + "where g.to_user_id = :userId\n"
                + "  and (o.status = 2 or o.status = 3)\n"
                + "order by c.money desc\n"
                + "limit 5")
        .bind("userId", userId)
        .as(String.class)
        .fetch()
        .all();
  }
}
