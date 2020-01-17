package com.gaea.single.bridge.core.lobo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.core.BusinessException;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DefaultLoboResultExchanger implements LoboResultExchanger {
  public <R> Mono<Result<R>> exchange(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter) {
    return mono.map(
            res -> {
              R result =
                  res.getDataCollection() != null
                      ? resConverter.convert(JsonUtils.toJsonObject(res.getDataCollection()))
                      : null;
              return Result.success(result);
            })
        .onErrorResume(
            (ex) -> {
              if (ex instanceof BusinessException) {
                log.info("业务异常 {}, {}", ((BusinessException) ex).getCode(), ex.getMessage());
                return Mono.just(Result.error(((BusinessException) ex).getCode(), ex.getMessage()));
              }
              log.info("系统内部错误", ex);
              return Mono.just(Result.error(ErrorCode.INNER_ERROR));
            });
  }

  public <R> Mono<Result<List<R>>> exchangeForList(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter) {
    return mono.map(
            res -> {
              List<R> result = new ArrayList<>();
              if (res.getDataCollection() != null) {
                JSONArray array = JsonUtils.toJsonArray(res.getDataCollection());
                result =
                    array.stream()
                        .map(item -> resConverter.convert((JSONObject) item))
                        .collect(Collectors.toList());
              }
              return Result.success(result);
            })
        .onErrorResume(
            ex -> {
              if (ex instanceof BusinessException) {
                log.info("业务异常 {}, {}", ((BusinessException) ex).getCode(), ex.getMessage());
                return Mono.just(Result.error(((BusinessException) ex).getCode(), ex.getMessage()));
              }
              log.info("系统内部错误", ex);
              return Mono.just(Result.error(ErrorCode.INNER_ERROR));
            });
  }
}
