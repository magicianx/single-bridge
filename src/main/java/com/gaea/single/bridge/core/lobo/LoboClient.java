package com.gaea.single.bridge.core.lobo;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.core.BusinessException;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LoboClient {
  private WebClient webClient;
  private LoboResultExchanger loboResultExchanger;

  public LoboClient(WebClient webClient, LoboResultExchanger loboResultExchanger) {
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

    MediaType mediaType = MediaType.APPLICATION_FORM_URLENCODED;
    BodyInserter<?, ? super ClientHttpRequest> bodyInserter = null;
    if (data != null) {
      boolean multipartForm = data.values().stream().anyMatch(v -> v instanceof FilePart);
      mediaType =
          multipartForm ? MediaType.MULTIPART_FORM_DATA : MediaType.APPLICATION_FORM_URLENCODED;

      bodyInserter = getBody(multipartForm, data);
    }

    return webClient
        .post()
        .uri(path)
        .contentType(mediaType)
        .header("userId", userId)
        .header("session", session)
        .body(bodyInserter)
        .retrieve()
        .bodyToMono(LoboResult.class)
        .flatMap(
            res -> {
              int code = res.getResultCode();
              if (LoboCode.isErrorCode(code)) {
                String message = LoboCode.getErrorMessage(res.getResultCode());
                log.error(
                    "请求lobo服务{} 失败: {}, {}",
                    path,
                    res.getResultCode(),
                    Optional.ofNullable(message).orElse("未知错误编码"));
                return Mono.error(
                    new BusinessException(
                        res.getResultCode(), Optional.ofNullable(message).orElse("请求失败，请稍候重试")));
              }
              log.info(
                  "请求lobo服务 {} 成功: {}, {}",
                  path,
                  res.getResultCode(),
                  LoboCode.getSuccessMessage(res.getResultCode()));
              return Mono.just(res);
            });
  }

  private BodyInserter<?, ? super ClientHttpRequest> getBody(
      boolean multipartForm, Map<String, Object> data) {
    if (multipartForm) {
      MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>(data.size());
      data.forEach(
          (k, v) ->
              formData.put(k, v == null ? Collections.emptyList() : Collections.singletonList(v)));
      return BodyInserters.fromMultipartData(formData);
    } else {
      MultiValueMap<String, String> formData = new LinkedMultiValueMap<>(data.size());
      data.forEach(
          (k, v) ->
              formData.put(
                  k,
                  v == null ? Collections.emptyList() : Collections.singletonList(v.toString())));
      return BodyInserters.fromFormData(formData);
    }
  }
}
