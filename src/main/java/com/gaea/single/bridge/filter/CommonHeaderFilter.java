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
    putAttribute(exchange, headers, CommonHeaderConst.USER_ID, "User-Id");
    putAttribute(exchange, headers, CommonHeaderConst.SESSION, "Session");
    return chain.filter(exchange);
  }

  private void putAttribute(
      ServerWebExchange exchange, HttpHeaders headers, String key, String headerName) {
    List<String> values = headers.get(headerName);
    if (values != null && !values.isEmpty()) {
      exchange.getAttributes().put(key, values.get(0));
    }
  }
}
