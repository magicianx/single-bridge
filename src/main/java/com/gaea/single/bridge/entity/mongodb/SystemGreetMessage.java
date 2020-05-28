package com.gaea.single.bridge.entity.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统级别打招呼消息
 *
 * @author cludy
 */
@Document("system_greet_message")
@Data
public class SystemGreetMessage {
  @Id private String id;
  private String content;
}
