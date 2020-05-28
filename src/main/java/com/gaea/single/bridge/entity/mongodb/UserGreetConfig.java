package com.gaea.single.bridge.entity.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/** @author cludy */
@Document("user_greet_config")
@Data
public class UserGreetConfig {
  @Id private String id;

  @Field("user_id")
  @Indexed
  private Long userId;

  @Field("custom_messages")
  private List<Message> customMessages;

  @Field("system_message_ids")
  private List<String> systemMessageIds;

  @Document
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Message {
    private String id;
    private String content;
  }
}
