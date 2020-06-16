package com.gaea.single.bridge.repository.mysql;

import com.gaea.single.bridge.entity.mysql.UserPersonalInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserPersonalInfoRepository extends R2dbcRepository<UserPersonalInfo, Long> {}
