package com.gaea.single.bridge.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.PlatformConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.platform.BannerRes;
import com.gaea.single.bridge.dto.platform.CheckVersionRes;
import com.gaea.single.bridge.dto.platform.SendSmsReq;
import com.gaea.single.bridge.enums.BannerType;
import com.gaea.single.bridge.enums.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "平台服务")
@Validated
public class PlatformController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/banners.net")
  @ApiOperation(value = "获取广告列表")
  public Mono<Result<List<BannerRes>>> getBanners(
      @ApiParam(value = "deviceType", required = true) @NotNull @RequestParam("deviceType")
          DeviceType deviceType,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("deviceType", deviceType.getCode());
            put("type", 2);
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.BANNER_LIST,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new BannerRes(
              result.getString("imgUrl"),
              result.getString("linkToUrl"),
              result.getString("title"),
              BannerType.INNER);
        });
  }

  @PostMapping(value = "/v1/sms_code.net", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "发送验证码")
  public Mono<Result<Object>> sendSmsCode(
      @Valid @RequestBody SendSmsReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", req.getType().getCode());
            put("userMobile", req.getPhoneNum());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.SEND_SMS_CODE, data, null);
  }

  @GetMapping(value = "/v1/checkVersion.net")
  @ApiOperation(value = "检查版本号")
  public Mono<Result<CheckVersionRes>> checkVersion(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", getOsType(exchange));
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.CHECK_VERSION, data, PlatformConverter.toCheckVersionRes);
  }
}
