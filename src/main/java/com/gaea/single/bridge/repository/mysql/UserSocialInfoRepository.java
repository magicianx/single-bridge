package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.UserSocialInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserSocialInfoRepository extends R2dbcRepository<UserSocialInfo, Long> {
  @Query(
      "select us.*\n"
          + "from user_social_info us\n"
          + "          join user_reg_info ur on us.user_id = ur.id\n"
          + "          join user_label_info ua on ua.user_id = ur.id\n"
          + "where ur.show_id = :showId\n"
          + "  and ur.status = 1\n"
          + "  and us.is_video_audit = 2\n"
          + "  and ua.label_id = :labelId")
  Mono<UserSocialInfo> findAnchorByShowId(String showId, Long labelId);

  @Query(
      "select us.*\n"
          + "from user_social_info us\n"
          + "         join user_reg_info ur on us.user_id = ur.id\n"
          + "where ur.show_id = :showId\n"
          + "  and ur.status = 1\n"
          + "  and us.is_video_audit != 2")
  Mono<UserSocialInfo> findGeneralUserByShowId(String showId);

  @Query("select * from user_social_info where user_id = :userId")
  Mono<UserSocialInfo> findByUserId(Long userId);
}
