package com.gaea.single.bridge.config.filter;

import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.constant.LoboRedisConstant;
import com.gaea.single.bridge.core.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证过滤器
 *
 * @author cludy
 */
@Component
@Slf4j
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('dev')")
public class AuthFilter extends AbstractFilter implements WebFilter {
  private static final String ALL_METHOD_MATCH = "*";

  @Qualifier("loboRedissonReactiveClient")
  @Autowired
  private RedissonReactiveClient redissonClient;

  @Autowired private ServiceProperties serviceProperties;
  private List<Pair<String, String>> authRequests;
  private PathMatcher pathMatcher = new AntPathMatcher();

  @PostConstruct
  public void init() {
    this.authRequests = resolveSessionAuthRequests();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if (authRequests == null || authRequests.isEmpty() || !isNeedAuth(exchange)) {
      return chain.filter(exchange);
    }

    String session = exchange.getAttribute(CommonHeaderConst.SESSION);
    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);
    log.info("正在过滤认证用户{}session: {}", userId, session);
    if (StringUtils.isBlank(session) || StringUtils.isBlank(userId)) {
      return completeWithCode(exchange, ErrorCode.INVALID_SESSION);
    }
    RMapReactive<String, String> userInfo =
        redissonClient.getMap(LoboRedisConstant.USER_LOGIN_INFO + userId, StringCodec.INSTANCE);
    return userInfo
        .get("lastLoginSession")
        .defaultIfEmpty("")
        .flatMap(
            loginSession -> {
              if (!loginSession.equals(session)) {
                log.info("用户 {} session不正确", userId);
                return completeWithCode(exchange, ErrorCode.INVALID_SESSION);
              }
              return chain.filter(exchange);
            });
  }

  private boolean isNeedAuth(ServerWebExchange exchange) {
    HttpMethod method = exchange.getRequest().getMethod();
    String path = exchange.getRequest().getPath().pathWithinApplication().value();
    return this.authRequests.stream()
        .anyMatch(
            r -> {
              boolean methodMatched =
                  ALL_METHOD_MATCH.equals(r.getLeft()) || method.matches(r.getLeft());
              return methodMatched && pathMatcher.match(r.getRight(), path);
            });
  }

  /**
   * 解析session认证配置
   *
   * @return
   */
  private List<Pair<String, String>> resolveSessionAuthRequests() {
    return serviceProperties.getAuth().getSession().stream()
        .map(
            def -> {
              String[] attrs = def.split(" ");

              return Pair.of(attrs[0], attrs[1]);
            })
        .collect(Collectors.toList());
  }
}
