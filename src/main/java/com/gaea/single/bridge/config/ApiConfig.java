package com.gaea.single.bridge.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@ConditionalOnProperty(value = "service.api.enable", havingValue = "true")
@Import(BeanValidatorPluginsConfiguration.class)
public class ApiConfig {
  @Autowired private TypeResolver typeResolver;

  @Bean
  ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Single API").version("1.0.0").build();
  }

  @Bean
  public Docket petApi(ApiInfo apiInfo) {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .genericModelSubstitutes(ResponseEntity.class)
        .alternateTypeRules(
            newRule(
                typeResolver.resolve(
                    DeferredResult.class,
                    typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                typeResolver.resolve(WildcardType.class)))
        .useDefaultResponseMessages(false)
        .forCodeGeneration(true)
        //        .globalResponseMessage(
        //            RequestMethod.GET,
        //            singletonList(
        //                new ResponseMessageBuilder()
        //                    .code(500)
        //                    .message("500 message")
        //                    .responseModel(new ModelRef("Error"))
        //                    .build()))
        //        .enableUrlTemplating(true)s
        .apiInfo(apiInfo);
    //        .globalOperationParameters(
    //            singletonList(
    //                new ParameterBuilder()
    //                    .name("someGlobalParameter")
    //                    .description("Description of someGlobalParameter")
    //                    .modelRef(new ModelRef("string"))
    //                    .parameterType("query")
    //                    .required(true)
    //                    .build()))
    //        .tags(new Tag("Pet Service", "All apis relating to pets"))
  }

  @Bean
  UiConfiguration uiConfig() {
    return UiConfigurationBuilder.builder()
        .deepLinking(true)
        .displayOperationId(false)
        .defaultModelsExpandDepth(1)
        .defaultModelExpandDepth(1)
        .defaultModelRendering(ModelRendering.EXAMPLE)
        .displayRequestDuration(false)
        .docExpansion(DocExpansion.NONE)
        .filter(false)
        .maxDisplayedTags(null)
        .operationsSorter(OperationsSorter.ALPHA)
        .showExtensions(false)
        .showCommonExtensions(false)
        .tagsSorter(TagsSorter.ALPHA)
        .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
        .validatorUrl(null)
        .build();
  }
}
