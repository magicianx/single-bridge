package com.gaea.single.bridge.core.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** @author cludy */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GreetUser implements Serializable {
  private static final long serialVersionUID = -2047534792893642467L;
  /** 用户id */
  private long userId;
  /** 云信id */
  private String yunxinId;
}
