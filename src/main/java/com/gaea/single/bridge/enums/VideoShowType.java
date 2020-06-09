package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频秀类型
 *
 * @author cludy
 */
@Getter
@AllArgsConstructor
public enum VideoShowType implements Metadata {
  RECOMMEND("推荐"),
  ONLINE("在线");

  private String desc;
}
