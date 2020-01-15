package com.gaea.single.bridge.support.lobo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.ErrorCodes;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.support.BusinessException;
import com.gaea.single.bridge.util.JsonUtils;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultLoboResultExchanger implements LoboResultExchanger {
  public <R> Mono<Result<R>> exchange(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter) {
    return mono.map(
            result ->
                Result.of(
                    result.getResultCode(),
                    resConverter.convert(JsonUtils.toJsonObject(result.getDataCollection())),
                    null))
        .onErrorResume(
            (ex) -> {
              if (ex instanceof BusinessException) {
                return Mono.just(Result.of(((BusinessException) ex).getErrorCode()));
              }
              return Mono.just(Result.of(ErrorCodes.INNER));
            });
  }

  public <R> Mono<Result<List<R>>> exchangeForList(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter) {
    return mono.map(
            result -> {
              JSONArray array = JsonUtils.toJsonArray(result.getDataCollection());
              List<R> data =
                  array.stream()
                      .map(item -> resConverter.convert((JSONObject) item))
                      .collect(Collectors.toList());
              return Result.of(result.getResultCode(), data, null);
            })
        .onErrorResume(
            (ex) -> {
              if (ex instanceof BusinessException) {
                return Mono.just(Result.of(((BusinessException) ex).getErrorCode()));
              }
              return Mono.just(Result.of(ErrorCodes.INNER));
            });
  }
}
