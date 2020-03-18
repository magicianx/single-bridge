package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 关注状态 */
@Getter
@AllArgsConstructor
public enum FollowStatus implements Metadata {
  FOLLOWED(1, "已关注"),
  UNFOLLOW(2, "未关注"),
  EACH_FOLLOW(3, "互相关注");

  private int code;
  private String desc;

  public static FollowStatus ofCode(int code) {
    for (FollowStatus status : FollowStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
