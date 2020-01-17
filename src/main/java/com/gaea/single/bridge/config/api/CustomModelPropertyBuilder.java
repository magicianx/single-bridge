package com.gaea.single.bridge.config.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static springfox.documentation.schema.Annotations.findPropertyAnnotation;

/**
 * 自定义ModelPropertyBuilderPlugin, 支持{@link ApiModelProperty#allowEmptyValue}的值为枚举名字
 *
 * @author wangqiu@lobochat.cn
 */
// @Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
@Slf4j
public class CustomModelPropertyBuilder implements ModelPropertyBuilderPlugin {
  private static final String ENUM_PACKAGE_NAME = "com.gaea.single.bridge.enums.Def";

  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }

  @Override
  public void apply(ModelPropertyContext context) {
    Optional<ApiModelProperty> annotation = empty();
    if (context.getAnnotatedElement().isPresent()) {
      annotation =
          annotation
              .map(Optional::of)
              .orElse(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
    }
    if (context.getBeanPropertyDefinition().isPresent()) {
      annotation =
          annotation
              .map(Optional::of)
              .orElse(
                  findPropertyAnnotation(
                      context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
    }
    if (annotation.isPresent()) {
      Optional<AllowableValues> allowableValues = annotation.map(toAllowableValues());
      allowableValues.ifPresent(values -> context.getBuilder().allowableValues(values));
    }
  }

  static Function<ApiModelProperty, AllowableValues> toAllowableValues() {
    return annotation -> {
      String allowableValues = annotation.allowableValues();
      // 如果只有一个值则检查是否为枚举
      if (StringUtils.hasText(allowableValues) && allowableValues.split(",").length == 1) {
        try {
          Class<?> cls = Class.forName(ENUM_PACKAGE_NAME + "." + allowableValues);
          @SuppressWarnings("rawtypes")
          Enum[] enums = (Enum[]) cls.getMethod("values").invoke(null);

          List<String> values =
              Arrays.stream(enums)
                  .map(
                      value -> {
                        try {
                          return value.name() + "(" + cls.getMethod("getDesc").invoke(value) + ")";
                        } catch (Exception e) {
                          throw new RuntimeException("");
                        }
                      })
                  .collect(Collectors.toList());
          return new AllowableListValues(values, "LIST");
          // 由于枚举不存在时，会抛出异常，这里不需做任何处理
        } catch (Exception ignored) {
        }
      }
      return null;
    };
  }

  public static Optional<ApiModelProperty> findApiModePropertyAnnotation(
      AnnotatedElement annotated) {
    Optional<ApiModelProperty> annotation = empty();

    if (annotated instanceof Method) {
      // If the annotated element is a method we can use this information to check superclasses as
      // well
      annotation =
          ofNullable(AnnotationUtils.findAnnotation(((Method) annotated), ApiModelProperty.class));
    }

    return annotation
        .map(Optional::of)
        .orElse(ofNullable(AnnotationUtils.getAnnotation(annotated, ApiModelProperty.class)));
  }
}
