package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.UserRegInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface UserRegInfoRepository extends R2dbcRepository<UserRegInfo, Long> {
  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit = 2 limit :start,:pageSize")
  Flux<UserRegInfo> listAuditPassedInfos(Integer os, String appId, int start, int pageSize);

  @Query(
      "select ur.yunxin_id\n"
          + "from user_reg_info ur\n"
          + "         join user_app_info ua on ur.id = ua.user_id\n"
          + "         join user_social_info us on ur.id = us.user_id\n"
          + "where ur.os = :os\n"
          + "  and ua.app_id = :appId\n"
          + "  and us.is_video_audit != 2 limit :start,:pageSize")
  Flux<UserRegInfo> listUnAuditPassedInfos(Integer os, String appId, int start, int pageSize);

  /**
   * 查询新注册用户
   *
   * @param appId 用户id
   * @param startDate 注册开始时间
   * @param endDate 注册结束时间
   * @return {@link Flux<UserRegInfo>}
   */
  @Query(
      " select u.*\n"
          + "from user_reg_info u\n"
          + "         join user_app_info a on u.id = a.user_id\n"
          + "where a.app_id = ?\n"
          + "  and u.status = 1\n"
          + "  and date(u.create_time) between ? and ?\n"
          + "order by create_time desc\n"
          + "\n")
  Flux<UserRegInfo> findNewRegisterUser(String appId, String startDate, String endDate);
}
