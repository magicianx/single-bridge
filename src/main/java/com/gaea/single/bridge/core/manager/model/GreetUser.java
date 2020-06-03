package com.gaea.single.bridge.core.manager.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/** @author cludy */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GreetUser implements Serializable {
  private static final long serialVersionUID = -2047534792893642467L;
  /** 用户id */
  private long userId;
  /** 云信id */
  private String yunxinId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    ;
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ;
    GreetUser greetUser = (GreetUser) o;
    return userId == greetUser.userId && Objects.equal(yunxinId, greetUser.yunxinId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userId, yunxinId);
  }
}
