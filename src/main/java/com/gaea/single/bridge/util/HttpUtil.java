package com.gaea.single.bridge.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

public final class HttpUtil {
  public static String getIp(ServerWebExchange exchange) {
    HttpHeaders headers = exchange.getRequest().getHeaders();
    String ips = headers.getFirst("X-Forwarded-For");
    if (StringUtils.isNotBlank(ips)) {
      return ips.split(",")[0];
    }

    return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
        .map(address -> address.getAddress().getHostAddress())
        .orElse(null);
  }
}
