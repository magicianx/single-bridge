package com.gaea.single.bridge.repository;

import com.gaea.single.bridge.entity.UserRegInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRegInfoRepository extends ReactiveCrudRepository<UserRegInfo, Long> {
  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit = 2")
  Flux<UserRegInfo> listAuditPassedYunXinIds(Integer os, String appId);

  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit != 2")
  Flux<UserRegInfo> listUnAuditPassedYunXinIds(Integer os, String appId);
}
