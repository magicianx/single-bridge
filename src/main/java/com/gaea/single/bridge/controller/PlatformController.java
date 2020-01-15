package com.gaea.single.bridge.controller;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.platform.BannerRes;
import com.gaea.single.bridge.enums.BannerType;
import com.gaea.single.bridge.enums.DeviceType;
import com.gaea.single.bridge.support.lobo.LoboClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "平台服务")
@Validated
public class PlatformController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/banners.net")
  @ApiOperation(value = "获取广告列表")
  public Mono<Result<BannerRes>> getBanners(
      @ApiParam(value = "deviceType", required = true) @NotNull @RequestParam("deviceType")
          DeviceType deviceType) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("deviceType", deviceType.getCode());
            put("type", 2);
          }
        };
    return loboClient.postForm(
        LoboPathConst.BANNER_LIST,
        data,
        (loboResult) ->
            new BannerRes(
                loboResult.getString("imgUrl"),
                loboResult.getString("linkToUrl"),
                BannerType.INNER));
  }
}
