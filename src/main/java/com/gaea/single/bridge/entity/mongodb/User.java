package com.gaea.single.bridge.entity.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** @author cludy */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
  @Id private Long id;
  /** 是否开启定位 */
  @Field("is_enable_position")
  private Boolean isEnablePosition;
  /** 是否可开启打招呼 */
  @Field("is_enable_greet")
  private Boolean isEnableGreet;
}
