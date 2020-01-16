package com.gaea.single.bridge.controller;

import com.gaea.single.bridge.constant.CommonHeaderConst;
import org.springframework.web.server.ServerWebExchange;

public abstract class BaseContoller {
  protected String getUserId(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.USER_ID);
  }

  protected String getAppId(ServerWebExchange exchange) {
    return exchange.getAttribute(CommonHeaderConst.APP_ID);
  }
}
