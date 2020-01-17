package com.gaea.single.bridge.controller.platform;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseContoller;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.platform.BannerRes;
import com.gaea.single.bridge.enums.BannerType;
import com.gaea.single.bridge.enums.DeviceType;
import com.gaea.single.bridge.core.lobo.LoboClient;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "平台服务")
@Validated
public class PlatformController extends BaseContoller {
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
        (result) ->
            new BannerRes(
                result.getString("imgUrl"), result.getString("linkToUrl"), BannerType.INNER));
  }
}
