package com.gaea.single.bridge.support.lobo;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.Result;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoboResultExchanger {
  /**
   * 将{@link LoboResult} 转化为业务响应对象
   *
   * @param mono {@link Mono<LoboResult>}
   * @param resConverter 响应类型转换
   * @param <R> 响应类型
   * @return {@link Mono< Result <R>>}
   */
  <R> Mono<Result<R>> exchange(Mono<LoboResult> mono, Converter<JSONObject, R> resConverter);

  /**
   * 将{@link LoboResult} 转化为业务响应对象列表
   *
   * @param mono {@link Mono<LoboResult>}
   * @param resConverter 响应类型转换
   * @param <R> 响应类型
   * @return {@link Mono<Result< List <R>>>}
   */
  <R> Mono<Result<List<R>>> exchangeForList(
      Mono<LoboResult> mono, Converter<JSONObject, R> resConverter);
}
