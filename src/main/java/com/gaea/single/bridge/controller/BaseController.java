package com.gaea.single.bridge.controller;

import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.dto.PageReq;
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

  protected Map<String, Object> getPageData(PageReq pageReq) {
    return new HashMap<String, Object>() {
      {
        put("pageNo", pageReq.getPageNum());
        put("pageSize", pageReq.getPageSize());
      }
    };
  }
}
