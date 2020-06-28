package com.gaea.single.bridge.core.lobo;

import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.core.error.BusinessException;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.util.HttpUtil;
import com.gaea.single.bridge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
import java.util.stream.Collectors;

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
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.POST, exchange, path, null, data, null)
        .transform(mono -> loboResultExchanger.exchange(mono, resConverter));
  }

  /**
   * 发送表单请求
   *
   * @param path 请求路径
   * @param params 请求参数
   * @param data 表单数据
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<R>> postForm(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      Map<String, Object> data,
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.POST, exchange, path, params, data, null)
        .transform(mono -> loboResultExchanger.exchange(mono, resConverter));
  }

  /**
   * 发送表单请求
   *
   * @param path 请求路径
   * @param data 表单数据
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @param loboErrorCodes lobo业务错误码
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<R>> postForm(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> data,
      Converter<Object, R> resConverter,
      List<Integer> loboErrorCodes) {
    return this.request(HttpMethod.POST, exchange, path, null, data, loboErrorCodes)
        .transform(mono -> loboResultExchanger.exchange(mono, resConverter));
  }

  /**
   * 发送分页表单请求
   *
   * @param path 请求路径
   * @param data 表单数据
   * @param resConverter lobo响应转化为{@link Result}
   * @param nestKey 内嵌的列表参数key
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<PageRes<R>>> postFormForPage(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> data,
      String nestKey,
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.POST, exchange, path, null, data, null)
        .transform(mono -> loboResultExchanger.exchangeForPage(mono, nestKey, resConverter));
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
      Converter<Object, R> resConverter) {
    return request(HttpMethod.POST, exchange, path, null, data, null)
        .transform(mono -> loboResultExchanger.exchangeForList(mono, resConverter));
  }

  /**
   * 发送get请求
   *
   * @param path 请求路径
   * @param params 请求参数
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<R>> get(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.GET, exchange, path, params, null, null)
        .transform(mono -> loboResultExchanger.exchange(mono, resConverter));
  }

  /**
   * 发送get请求,响应结果为列表
   *
   * @param path 请求路径
   * @param params 请求参数
   * @param resConverter lobo响应转化为{@link Result}
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<List<R>>>}
   */
  public <R> Mono<Result<List<R>>> getForList(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.GET, exchange, path, params, null, null)
        .transform(mono -> loboResultExchanger.exchangeForList(mono, resConverter));
  }

  /**
   * 发送get分页请求
   *
   * @param path 请求路径
   * @param params 请求参数
   * @param resConverter lobo响应转化为{@link Result}
   * @param nestKey 内嵌的列表参数key
   * @param <R> 业务响应类型
   * @return {@link Mono<Result<R>>}
   */
  public <R> Mono<Result<PageRes<R>>> getForPage(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      String nestKey,
      Converter<Object, R> resConverter) {
    return this.request(HttpMethod.GET, exchange, path, params, null, null)
        .transform(mono -> loboResultExchanger.exchangeForPage(mono, nestKey, resConverter));
  }

  private Mono<LoboResult> request(
      HttpMethod method,
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      Map<String, Object> data,
      List<Integer> loboErrorCodes) {

    Mono<String> mono;
    if (HttpMethod.GET.equals(method)) {
      mono = doGet(exchange, path, params);
    } else if (HttpMethod.POST.equals(method)) {
      mono = doPostForm(exchange, path, params, data);
    } else {
      throw new UnsupportedOperationException(method.name() + " not support");
    }

    return mono.flatMap(
        body -> {
          log.info("请求lobo服务 {} 完成: {}", path, body);
          LoboResult res = JsonUtils.toObject(body, LoboResult.class);

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
          String message = LoboCode.getSuccessMessage(res.getResultCode());
          log.info("请求lobo服务 {} 成功: {}, {}", path, res.getResultCode(), message);

          if (loboErrorCodes != null && loboErrorCodes.contains(code)) {
            return Mono.error(new BusinessException(code, message));
          }
          return Mono.just(res);
        });
  }

  private Mono<String> doGet(ServerWebExchange exchange, String path, Map<String, Object> params) {
    String fullPath = getFullPath(path, params);

    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);
    String session = exchange.getAttribute(CommonHeaderConst.SESSION);
    String ip = HttpUtil.getIp(exchange);

    log.info(
        "正在请求lobo服务 {}: header {}, params {}",
        fullPath,
        String.format(
            "{\"userId\": \"%s\",\"session\": \"%s\",\"x-forwarded-for\": \"%s\"}",
            userId, session, ip),
        params != null ? JsonUtils.toJsonString(params) : null);

    return webClient
        .get()
        .uri(fullPath)
        .header("userId", userId)
        .header("userid", userId) // 8020使用的是userid
        .header("session", session)
        .header("x-forwarded-for", ip)
        .retrieve()
        .bodyToMono(String.class);
  }

  private Mono<String> doPostForm(
      ServerWebExchange exchange,
      String path,
      Map<String, Object> params,
      Map<String, Object> data) {
    String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);
    String session = exchange.getAttribute(CommonHeaderConst.SESSION);
    String fullPath = getFullPath(path, params);
    String ip = HttpUtil.getIp(exchange);

    MediaType mediaType = MediaType.APPLICATION_FORM_URLENCODED;
    BodyInserter<?, ? super ClientHttpRequest> bodyInserter = null;
    boolean multipartForm = false;
    if (data != null) {
      multipartForm =
          data.values().stream().anyMatch(v -> v instanceof HttpEntity || v instanceof FilePart);
      mediaType =
          multipartForm ? MediaType.MULTIPART_FORM_DATA : MediaType.APPLICATION_FORM_URLENCODED;

      bodyInserter = getBody(multipartForm, data);
    }

    if (!multipartForm) {
      log.info(
          "正在请求lobo服务 {}: header {}, params {}, data {}",
          fullPath,
          String.format(
              "{\"userId\": \"%s\",\"session\": \"%s\",\"x-forwarded-for\": \"%s\" }",
              userId, session, ip),
          params != null ? JsonUtils.toJsonString(params) : null,
          JsonUtils.toJsonString(data));
    }

    return webClient
        .post()
        .uri(fullPath)
        .contentType(mediaType)
        .header("userId", userId)
        .header("userid", userId) // 8020使用的是userid
        .header("session", session)
        .header("x-forwarded-for", ip)
        .body(bodyInserter)
        .retrieve()
        .bodyToMono(String.class);
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

  private String getFullPath(String path, Map<String, Object> params) {
    if (params != null) {
      String pathParams =
          params.entrySet().stream()
              .filter(entry -> entry.getKey() != null && entry.getValue() != null)
              .map(entry -> entry.getKey() + "=" + entry.getValue())
              .collect(Collectors.joining("&"));
      return path + "?" + pathParams;
    }
    return path;
  }
}
