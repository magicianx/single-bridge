package com.gaea.single.bridge.entity.mysql;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserRegInfo {
  @Id private Long id;
  private String showId;
  private String mobilePhone;
  private String password;
  /** 状态，1：有效，2：删除，3：被冻结 */
  private Integer status;

  private Integer channel;
  private String qqToken;
  private String wechatToken;
  private String wechatUnionId;
  private String yunxinId;
  private LocalDateTime createTime;
  private LocalDateTime lastLoginTime;
  private LocalDateTime lastTradeTime;
  private LocalDateTime lastModifyTime;
  private String os;
  private String deviceNo;
}
