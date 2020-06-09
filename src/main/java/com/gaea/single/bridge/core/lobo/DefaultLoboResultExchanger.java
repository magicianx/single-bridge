package com.gaea.single.bridge.core.lobo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.core.error.BusinessException;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultLoboResultExchanger implements LoboResultExchanger {
  public <R> Mono<Result<R>> exchange(Mono<LoboResult> mono, Converter<Object, R> resConverter) {
    return mono.map(
            res -> {
              R result = null;
              if (resConverter != null) {
                result =
                    isOriginalType(res)
                        ? resConverter.convert(JsonUtils.toJsonObject(res.getDataCollection()))
                        : resConverter.convert(res.getDataCollection());
              }
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

  @Override
  public <R> Mono<Result<PageRes<R>>> exchangeForPage(
      Mono<LoboResult> mono, String nestKey, Converter<Object, R> resConverter) {
    return mono.map(
            res -> {
              List<R> result = getResultList(res, nestKey, resConverter);
              return Result.success(PageRes.from(res, result));
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
      Mono<LoboResult> mono, Converter<Object, R> resConverter) {
    return mono.map(
            res -> {
              List<R> result = getResultList(res, null, resConverter);
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

  private <R> List<R> getResultList(
      LoboResult res, String nestKey, Converter<Object, R> resConverter) {
    List<R> result = new ArrayList<>();
    if (res.getDataCollection() != null && resConverter != null) {
      JSONArray array =
          StringUtils.isNotBlank(nestKey)
              ? ((JSONObject) res.getDataCollection()).getJSONArray(nestKey)
              : JsonUtils.toJsonArray(res.getDataCollection());
      result = array.stream().map(resConverter::convert).collect(Collectors.toList());
    }
    return result;
  }

  private boolean isOriginalType(LoboResult res) {
    return res.getDataCollection() instanceof JSONObject || res.getDataCollection() instanceof Map;
  }
}
