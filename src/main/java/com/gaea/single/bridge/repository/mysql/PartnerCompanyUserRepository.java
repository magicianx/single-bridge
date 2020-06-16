package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.PartnerCompanyUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PartnerCompanyUserRepository extends R2dbcRepository<PartnerCompanyUser, Long> {
  @Query("select count(*) from partner_company_user where user_id = :userId")
  Mono<Integer> countByUserId(Long userId);
}
