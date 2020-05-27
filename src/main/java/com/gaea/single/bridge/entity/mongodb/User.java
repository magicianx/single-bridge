package com.gaea.single.bridge.entity.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** @author cludy */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
  @Id private Long id;
  /** 是否开启定位 */
  private Boolean isEnablePosition;
}
