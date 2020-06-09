package com.gaea.single.bridge.entity.mysql.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * 用户基本信息
 *
 * @author cludy
 */
@Data
public class UserBaseInfo {
  private Long userId;
  private String nickName;

  @Column("yunxin_id")
  private String yunXinId;

  private String portrait;
  private Integer isVideoAudit;
}
