package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.AnchorPriceItemRes;
import com.gaea.single.bridge.dto.user.SetAnchorPriceReq;
import com.gaea.single.bridge.util.LoboUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/price", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "主播价格服务")
@Validated
public class PriceController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/list.do")
  @ApiOperation(value = "获取主播单价列表")
  public Mono<Result<List<AnchorPriceItemRes>>> getAnchorPrices(
      @ApiIgnore ServerWebExchange exchange) {
    return loboClient.getForList(
        exchange,
        LoboPathConst.GET_ANCHOR_PRICES,
        null,
        (obj -> {
          JSONObject result = (JSONObject) obj;
          return new AnchorPriceItemRes(
              result.getLong("id"),
              result.getInteger("price"),
              result.getInteger("grade"),
              LoboUtil.toBoolean(result.getInteger("isCheck")));
        }));
  }

  @PostMapping(value = "/v1/set.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "设置主播单价")
  public Mono<Result<Object>> setAnchorPrice(
      @Valid @RequestBody SetAnchorPriceReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("price", req.getVideoPrice());
            put("grade", req.getVideoGrade());
            put("audioPrice", "300");
            put("audioGrade", "0");
          }
        };

    return loboClient.postForm(exchange, LoboPathConst.SET_ANCHOR_PRICE, data, null);
  }
}
