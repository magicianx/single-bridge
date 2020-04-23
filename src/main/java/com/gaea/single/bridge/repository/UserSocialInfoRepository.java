package com.gaea.single.bridge.repository;

import com.gaea.single.bridge.entity.UserSocialInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserSocialInfoRepository extends ReactiveCrudRepository<UserSocialInfo, Long> {
  @Query(
      "select us.*\n"
          + "from user_social_info us\n"
          + "         left join user_reg_info ur on us.user_id = ur.id\n"
          + "where ur.show_id = :showId\n"
          + "  and ur.status = 1\n"
          + "  and us.is_video_audit = 1")
  Mono<UserSocialInfo> findAnchorByShowId(String showId);

  @Query(
      "select us.*\n"
          + "from user_social_info us\n"
          + "         left join user_reg_info ur on us.user_id = ur.id\n"
          + "where ur.show_id = :showId\n"
          + "  and ur.status = 1\n"
          + "  and us.is_video_audit != 1")
  Mono<UserSocialInfo> findGeneralUserByShowId(String showId);
}
