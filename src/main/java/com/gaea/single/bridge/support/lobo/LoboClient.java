package com.gaea.single.bridge.support.lobo;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.constant.ErrorCodes;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class LoboClient {
  private static final int SUCCESS_CODE = 1400;
  private WebClient webClient;
  private DefaultLoboResultExchanger loboResultExchanger;

  public LoboClient(WebClient webClient, DefaultLoboResultExchanger loboResultExchanger) {
    this.webClient = webClient;
    this.loboResultExchanger = loboResultExchanger;
  }

  /**
   * 发送表单请求
   *
   * @param path 请求路径
   * @param data 表单数据
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<R>> postForm(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> data,
      Converter<JSONObject, R> resConverter) {
    return this.doPostForm(exchange, path, data)
        .transform(mono -> loboResultExchanger.exchange(mono, resConverter));
  }

  /**
   * 发送表单请求
   *
   * @param path 请求路径
   * @param data 表单数据
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<List<R>>> postFormForList(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> data,
      Converter<JSONObject, R> resConverter) {
    return this.doPostForm(exchange, path, data)
        .transform(mono -> loboResultExchanger.exchangeForList(mono, resConverter));
  }

  public Mono<LoboResult> doPostForm(
      ServerWebExchange exchange, String path, Map<String, Object> data) {
    log.info("正在请求lobo服务 {}", path);
    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);
    String session = exchange.getAttribute(CommonHeaderConst.SESSION);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>(data.size());
    data.forEach(
        (k, v) ->
            formData.put(
                k, v == null ? Collections.emptyList() : Collections.singletonList(v.toString())));

    return webClient
        .post()
        .uri(path)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("userId", userId)
        .header("session", session)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(LoboResult.class)
        .flatMap(
            res -> {
              if (res.getResultCode() != SUCCESS_CODE) {
                log.error(String.format("请求lobo服务%s 失败: %s", path, res.getResultCode()));
                return Mono.error(ErrorCodes.INNER_ERROR.newBusinessException());
              }
              log.info("请求lobo服务 {} 成功", path);
              return Mono.just(res);
            });
  }
}
