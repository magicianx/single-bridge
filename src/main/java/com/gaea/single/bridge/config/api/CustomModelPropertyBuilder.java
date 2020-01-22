package com.gaea.single.bridge.config.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自定义ModelPropertyBuilderPlugin, 为枚举类型字段添加中文描述
 *
 * @author wangqiu@lobochat.cn
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
@Slf4j
public class CustomModelPropertyBuilder extends AbstractEnumTypeBuilder
    implements ModelPropertyBuilderPlugin {
  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }

  @Override
  @SuppressWarnings("unchecked,rawtypes")
  public void apply(ModelPropertyContext context) {
    context
        .getBeanPropertyDefinition()
        .ifPresent(
            (definition) -> {
              Class resultType = definition.getField().getType().getRawClass();
              if (!isMetadata(resultType)) {
                return;
              }
              context.getBuilder().allowableValues(getAllowableValues(resultType));
            });
  }
}
