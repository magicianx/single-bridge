package com.gaea.single.bridge.entity.mysql;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户个人信息
 *
 * @author cludy
 */
@Data
public class UserPersonalInfo implements Serializable {
  private static final long serialVersionUID = -8762918066579165800L;

  @Id private Long userId;
  private Integer sex;
  private String cityCode;
  private LocalDateTime birthDate;
  private String idCardNum;
  private String realName;
  private Integer isRealNameAuth;
  private LocalDateTime createTime;
  private LocalDateTime lastModifyTime;
}
