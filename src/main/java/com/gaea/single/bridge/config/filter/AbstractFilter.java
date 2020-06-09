package com.gaea.single.bridge.config.filter;

import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.util.JsonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

/** @author cludy */
public abstract class AbstractFilter {

  /**
   * 请求完成，将响应对象写入response中
   *
   * @param exchange {@link ServerWebExchange}
   * @param errorCode {@link ErrorCode} 错误码
   * @return {@link Mono<Void>}
   */
  protected static Mono<Void> completeWithCode(ServerWebExchange exchange, ErrorCode errorCode) {
    byte[] body = JsonUtils.toJsonBytes(Result.error(errorCode));
    HttpHeaders headers = exchange.getResponse().getHeaders();
    headers.put(
        HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
    exchange.getResponse().setStatusCode(HttpStatus.OK);

    return exchange
        .getResponse()
        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body)))
        .flatMap(v -> exchange.getResponse().setComplete());
  }
}
