package com.gaea.single.bridge.entity.mysql;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/** 公会用户 */
@Data
public class PartnerCompanyUser {
  @Id private Long id;
  private Integer companyId;
  private Integer userId;
  private Integer comStatus;
  private Date auditTime;
}
