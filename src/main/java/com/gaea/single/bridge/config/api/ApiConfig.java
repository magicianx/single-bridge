package com.gaea.single.bridge.config.api;

import com.fasterxml.classmate.TypeResolver;
import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.dto.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.util.Arrays;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@ConditionalOnProperty(value = "service.api.enable", havingValue = "true")
@Import(BeanValidatorPluginsConfiguration.class)
public class ApiConfig {
  @Autowired private TypeResolver typeResolver;
  @Autowired private ServiceProperties serviceProperties;

  @Bean
  ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Single API").version("1.1.0").build();
  }

  @Bean
  public Docket petApi(ApiInfo apiInfo) throws Exception {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        //        .genericModelSubstitutes(Mono.class, Flux.class, Publisher.class)
        .alternateTypeRules(
            newRule(
                typeResolver.resolve(
                    Mono.class, typeResolver.resolve(Result.class, WildcardType.class)),
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
        //        .enableUrlTemplating(true)
        .apiInfo(apiInfo)
        .globalOperationParameters(
            Arrays.asList(
                getHeaderParameter(CommonHeaderConst.USER_ID, "用户id", false, "734123"),
                getHeaderParameter(
                    CommonHeaderConst.SESSION,
                    "用户session",
                    false,
                    "ffd2bfe74c664d0880989482daa4ceab"),
                getHeaderParameter(
                    CommonHeaderConst.APP_VERSION, "应用版本号, 应用版本号和混淆应用版本号只需传一个即可", false, "1.1.0"),
                getHeaderParameter(CommonHeaderConst.CHANNEL_ID, "渠道id", true, "9536"),
                getHeaderParameter(CommonHeaderConst.DEVICE_TYPE, "设备型号", true, "1"),
                getHeaderParameter(
                    CommonHeaderConst.DEVICE_NO, "设备号, ios: IDFV, android: IMEI", true, "1"),
                getHeaderParameter(
                    CommonHeaderConst.OS_TYPE,
                    "操作系统类型, ios: IOS, android: ANDROID",
                    true,
                    "ANDROID"),
                getHeaderParameter(
                    CommonHeaderConst.PACKAGE_NAME, "包名", true, "com.Ramsey.LoveBubble"),
                getHeaderParameter(
                    CommonHeaderConst.Cav, "混淆应用版本号, 应用版本号和混淆应用版本号只需传一个即可", false, "")));
    //        .tags(new Tag("Pet Service", "All apis relating to pets"))
  }

  private Parameter getHeaderParameter(
      String name, String desc, boolean required, String defaultValue) {
    return new ParameterBuilder()
        .name(name)
        .description(desc)
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .required(required)
        .defaultValue(defaultValue)
        .build();
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
