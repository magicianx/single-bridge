package com.gaea.single.bridge.constant;

/** 请求公共请求头定义 */
public interface CommonHeaderConst {
  String USER_ID = "User-Id"; // 用户id
  String SESSION = "Session"; // 用户session
  String APP_VERSION = "App-Version"; // 客户端版本号
  String CHANNEL_ID = "Channel-Id"; // 渠道id
  String DEVICE_TYPE = "Device-Type"; // 设备型号
  String DEVICE_NO = "Device-No"; // 设备号, ios: IDFV, android: IMEI
  String OS_TYPE = "OS-Type"; // 操作系统类型
  String PACKAGE_NAME = "Package-Name"; // 包名
}
