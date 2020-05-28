package com.gaea.single.bridge.controller;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.core.ComplexAppVersionFactory;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.enums.OsType;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

public abstract class BaseController {
  @Autowired ComplexAppVersionFactory appVersionFactory;

  protected Long getUserId(ServerWebExchange exchange) {
    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);

    if (StringUtils.isNotBlank(userId)) {
      return Long.valueOf(userId);
    }

    return null;
  }

  protected String getAppId() {
    return DictionaryProperties.get().getLobo().getAppId();
  }

  protected String getChannelId(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.CHANNEL_ID);
  }

  protected String getAppVersion(ServerWebExchange exchange) {
    String version = exchange.getAttribute(CommonHeaderConst.Cav);
    if (StringUtils.isNotBlank(version)) {
      return appVersionFactory.getAppVersion(version);
    }
    return exchange.getAttribute(CommonHeaderConst.APP_VERSION);
  }

  protected String getSession(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.SESSION);
  }

  protected String getDeviceType(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.DEVICE_TYPE);
  }

  protected String getPackageName(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.PACKAGE_NAME);
  }

  protected OsType getOsType(ServerWebExchange exchange) {
    return OsType.valueOf(exchange.getAttribute(CommonHeaderConst.OS_TYPE));
  }

  protected String getDeviceNo(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.DEVICE_NO);
  }

  protected Map<String, Object> getPageData(PageReq pageReq) {
    return getPageData(pageReq.getPageNum(), pageReq.getPageSize());
  }

  protected Map<String, Object> getPageData(int pageNum, int pageSize) {
    return ImmutableMap.<String, Object>builder()
        .put("pageNo", pageNum)
        .put("pageSize", pageSize)
        .build();
  }
}
