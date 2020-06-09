package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 请求公共请求头定义
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonHeaderConst {
  /** 用户id */
  public static final String USER_ID = "User-Id";
  /** 用户session */
  public static final String SESSION = "Session";
  /** 客户端版本号 */
  public static final String APP_VERSION = "App-Version";
  /** 渠道id */
  public static final String CHANNEL_ID = "Channel-Id";
  /** 设备型号 */
  public static final String DEVICE_TYPE = "Device-Type";
  /** 设备号, ios: IDFV, android: IMEI */
  public static final String DEVICE_NO = "Device-No";
  /** 操作系统类型 */
  public static final String OS_TYPE = "OS-Type";
  /** 包名 */
  public static final String PACKAGE_NAME = "Package-Name";
  /** 混淆版本号，避免ios审核的时候被修改版本号探测隐藏功能，这里使用base64对版本号编码 */
  public static final String Cav = "Cav";
}
