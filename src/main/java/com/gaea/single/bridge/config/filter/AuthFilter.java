package com.gaea.single.bridge.config.filter;

import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.constant.CacheConstant;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全过滤器
 *
 * @author cludy
 */
//@Component
@Slf4j
public class AuthFilter implements WebFilter {
  @Qualifier("loboRedissonReactiveClient")
  @Autowired
  private RedissonReactiveClient redissonClient;

  @Autowired private ServiceProperties serviceProperties;

  private List<Pair<HttpMethod, String>> sessionAuthRequests;

  @PostConstruct
  public void init() {
    this.sessionAuthRequests = resolveSessionAuthRequests();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if (sessionAuthRequests == null || sessionAuthRequests.isEmpty() || !isNeedAuth(exchange)) {
      return chain.filter(exchange);
    }

    String session = exchange.getAttribute(CommonHeaderConst.SESSION);
    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);

    if (StringUtils.isNotBlank(session)) {

      RMapReactive<String, String> userInfo = redissonClient.getMap(CacheConstant.USER_LOGIN_INFO);
//      userInfo.get("lastLoginSession").map(loginSession -> loginSession.equals(session));
    }
    return chain.filter(exchange);
  }

  private boolean isNeedAuth(ServerWebExchange exchange) {
    HttpMethod method = exchange.getRequest().getMethod();
    String path = exchange.getRequest().getPath().contextPath().value();
    return this.sessionAuthRequests.stream()
        .anyMatch(r -> r.getLeft() == method || r.getRight().equals(path));
  }

  /**
   * 解析session认证配置
   *
   * @return
   */
  private List<Pair<HttpMethod, String>> resolveSessionAuthRequests() {
    return serviceProperties.getAuth().getSession().stream()
        .map(
            def -> {
              String[] attrs = def.split(" ");

              return Pair.of(HttpMethod.valueOf(attrs[0]), attrs[1]);
            })
        .collect(Collectors.toList());
  }
}
