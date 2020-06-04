package com.gaea.single.bridge.repository;

import com.gaea.single.bridge.entity.UserRegInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface UserRegInfoRepository extends R2dbcRepository<UserRegInfo, Long> {
  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit = 2 limit :start,:pageSize")
  Flux<UserRegInfo> listAuditPassedYunXinIds(Integer os, String appId, int start, int pageSize);

  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit != 2 limit :start,:pageSize")
  Flux<UserRegInfo> listUnAuditPassedYunXinIds(Integer os, String appId, int start, int pageSize);
}
