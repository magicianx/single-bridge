package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.model.UserBaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collection;

/** @author cludy */
@Component
public class UserCompositeRepository {
  @Autowired private DatabaseClient databaseClient;

  /**
   * 根据用户id列表查询用户基本信息
   *
   * @param userIds 用户id列表
   * @return {@link Flux<UserBaseInfo>}
   */
  public Flux<UserBaseInfo> listBaseInfoByUserIds(@Param("userIds") Collection<Long> userIds) {
    return databaseClient
        .execute(
            "select s.user_id, s.nick_name, u.yunxin_id, s.portrait, s.is_video_audit\n"
                + "from user_reg_info u\n"
                + "         join user_social_info s on u.id = s.user_id\n"
                + "where u.id in (:userIds)")
        .bind("userIds", userIds)
        .as(UserBaseInfo.class)
        .fetch()
        .all();
  }
}
