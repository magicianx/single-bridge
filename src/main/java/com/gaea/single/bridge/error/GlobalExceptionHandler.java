package com.gaea.single.bridge.error;

import com.gaea.single.bridge.constant.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/** 捕获全局异常, 设置返回状态为200, 并转化为与{@link com.gaea.single.bridge.dto.Result} 相同的格式 */
@Component
@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
  @Autowired
  public GlobalExceptionHandler(
      ErrorAttributes errorAttributes,
      ResourceProperties resourceProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer serverCodecConfigurer,
      ObjectProvider<ViewResolver> viewResolvers) {
    super(errorAttributes, resourceProperties, applicationContext);
    super.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return route(all(), this::renderErrorResponse);
  }

  protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
    Map<String, Object> error = getErrorAttributes(request, true);
    log.error("系统异常: " + error.get("trace"));

    Map<String, Object> result = new HashMap<>();
    result.put("code", ErrorCodes.INNER_ERROR.getCode());
    result.put("message", ErrorCodes.INNER_ERROR.getMassage());
    result.put("data", null);

    return ServerResponse.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(result));
  }
}
