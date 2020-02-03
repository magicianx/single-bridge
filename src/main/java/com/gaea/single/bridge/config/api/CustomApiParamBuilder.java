package com.gaea.single.bridge.config.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "prod")
@Component
@Order(SWAGGER_PLUGIN_ORDER + 1)
public class CustomApiParamBuilder extends AbstractEnumTypeBuilder
    implements ParameterBuilderPlugin {
  @Override
  public void apply(ParameterContext context) {
    Class<?> paramType = context.resolvedMethodParameter().getParameterType().getErasedType();

    if (!isMetadata(paramType)) {
      return;
    }
    context.parameterBuilder().allowableValues(getAllowableValues(paramType));
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }
}
