package com.gaea.single.bridge.config.api;

import com.gaea.single.bridge.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.HashSet;
import java.util.Set;

/** 解析{@link ApiErrorCode}注解，转化为swagger响应码 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerResponseCodeBuilder implements OperationBuilderPlugin {
  @Override
  public void apply(OperationContext context) {
    Set<ResponseMessage> responseMessages = new HashSet<>();
    context
        .findAnnotation(ApiErrorCode.class)
        .ifPresent(
            apiErrorCode -> {
              for (ErrorCode errorCode : apiErrorCode.value()) {
                responseMessages.add(
                    new ResponseMessageBuilder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
              }
            });
    context.operationBuilder().responseMessages(responseMessages);
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }
}
