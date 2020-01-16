package com.gaea.single.bridge.support.lobo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.ErrorCodes;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.support.BusinessException;
import com.gaea.single.bridge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DefaultLoboResultExchanger implements LoboResultExchanger {
  public <R> Mono<Result<R>> exchange(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter) {
    return mono.map(
            result ->
                Result.success(
                    resConverter.convert(JsonUtils.toJsonObject(result.getDataCollection()))))
        .onErrorResume(
            (ex) -> {
              if (ex instanceof BusinessException) {
                ErrorCode errorCode = ((BusinessException) ex).getErrorCode();
                log.info("业务异常 {}, {}", errorCode.getCode(), errorCode.getMassage());
                return Mono.just(Result.error(errorCode));
              }
              log.info("系统内部错误", ex);
              return Mono.just(Result.error(ErrorCodes.INNER_ERROR));
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
              return Result.success(data);
            })
        .onErrorResume(
            ex -> {
              if (ex instanceof BusinessException) {
                ErrorCode errorCode = ((BusinessException) ex).getErrorCode();
                log.info("业务异常 {}, {}", errorCode.getCode(), errorCode.getMassage());
                return Mono.just(Result.error(errorCode));
              }
              log.info("系统内部错误", ex);
              return Mono.just(Result.error(ErrorCodes.INNER_ERROR));
            });
  }
}
