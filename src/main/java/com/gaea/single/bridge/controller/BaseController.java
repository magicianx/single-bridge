package com.gaea.single.bridge.controller;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.enums.OsType;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
  protected String getUserId(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.USER_ID);
  }

  protected String getAppId() {
    return DictionaryProperties.get().getLobo().getAppId();
  }

  protected String getChannelId() {
    return DictionaryProperties.get().getLobo().getChannelId();
  }

  protected String getAppVersion(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.APP_VERSION);
  }

  protected String getSession(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.SESSION);
  }

  protected String getDeviceType(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.DEVICE_TYPE);
  }

  protected String getPacketName(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.PACKAGE_NAME);
  }

  protected OsType getOsType(ServerWebExchange exchange) {
    return OsType.valueOf(exchange.getAttribute(CommonHeaderConst.OS_TYPE));
  }

  protected String getDeviceNo(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.DEVICE_NO);
  }

  protected Map<String, Object> getPageData(PageReq pageReq) {
    return new HashMap<String, Object>() {
      {
        put("pageNo", pageReq.getPageNum());
        put("pageSize", pageReq.getPageSize());
      }
    };
  }
}
