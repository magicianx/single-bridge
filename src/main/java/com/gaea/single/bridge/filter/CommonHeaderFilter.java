package com.gaea.single.bridge.filter;

import com.gaea.single.bridge.constant.CommonHeaderConst;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/** 将所有请求中的公共请求头参数放到{@link ServerWebExchange}的attribute中 */
@Component
public class CommonHeaderFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    HttpHeaders headers = exchange.getRequest().getHeaders();
    putAttribute(exchange, headers, CommonHeaderConst.USER_ID);
    putAttribute(exchange, headers, CommonHeaderConst.SESSION);
    putAttribute(exchange, headers, CommonHeaderConst.APP_VERSION);
    putAttribute(exchange, headers, CommonHeaderConst.DEVICE_TYPE);
    putAttribute(exchange, headers, CommonHeaderConst.DEVICE_NO);
    putAttribute(exchange, headers, CommonHeaderConst.OS_TYPE);
    putAttribute(exchange, headers, CommonHeaderConst.PACKAGE_NAME);
    putAttribute(exchange, headers, CommonHeaderConst.CHANNEL_ID);
    putAttribute(exchange, headers, CommonHeaderConst.Cav);
    return chain.filter(exchange);
  }

  private void putAttribute(ServerWebExchange exchange, HttpHeaders headers, String key) {
    List<String> values = headers.get(key);
    if (values != null && !values.isEmpty()) {
      exchange.getAttributes().put(key, values.get(0));
    }
  }
}
