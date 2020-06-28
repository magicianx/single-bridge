package com.gaea.single.bridge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * 用户真实状态
 *
 * @author cludy
 */
@AllArgsConstructor
@Getter
public enum UserRealOnlineStatus {
  ONLINE(1, "在线"),
  OFFLINE(0, "离线");

  private final int index;
  private final String value;

  /**
   * 根据index获取 {@link UserRealOnlineStatus}
   *
   * @param index
   * @return {@link UserRealOnlineStatus}
   */
  public static Optional<UserRealOnlineStatus> ofIndex(Integer index) {
    return Optional.ofNullable(index)
        .map(
            i -> {
              for (UserRealOnlineStatus status : UserRealOnlineStatus.values()) {
                if (status.getIndex() == index) {
                  return status;
                }
              }
              return null;
            });
  }
}
