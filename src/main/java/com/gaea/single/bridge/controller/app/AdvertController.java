package com.gaea.single.bridge.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.app.PopupAdvertRes;
import com.gaea.single.bridge.enums.AdvertType;
import com.gaea.single.bridge.util.LoboUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/app/advert", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "广告服务")
@Validated
public class AdvertController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/popup.net")
  @ApiOperation(value = "获取弹窗广告")
  public Mono<Result<PopupAdvertRes>> getPopupAdvert(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("deviceType", getOsType(exchange));
            put("userId", getUserId(exchange));
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.GET_POPUP_ADVERT,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          AdvertType advertType =
              LoboUtil.toBoolean2(result.getInteger("isExternalLink"))
                  ? AdvertType.OUTER
                  : AdvertType.INNER;

          return new PopupAdvertRes(
              result.getString("title"),
              advertType,
              result.getString("imgUrl"),
              result.getString("linkToUrl"));
        });
  }
}
