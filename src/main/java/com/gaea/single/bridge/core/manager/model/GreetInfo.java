package com.gaea.single.bridge.core.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/** @author cludy */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GreetInfo implements Serializable {
  private static final long serialVersionUID = 6019891088019163648L;
  /** 打招呼对的次数 */
  private Integer greetTimes;
  /** 最后一次打招呼的时间 */
  private LocalDateTime lastGreetTime;
}
