package com.gaea.single.bridge.controller.account;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.PayAmountOptionRes;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.PayWay;
import com.gaea.single.bridge.util.Md5Utils;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/account/pay", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "支付服务")
@Validated
public class PayController extends BaseController {
  @Autowired private LoboClient loboClient;
  private final String key = Md5Utils.encrypt("huoquchongzhijinelieb");

  @GetMapping(value = "/v1/options.do")
  @ApiOperation(value = "获取充值金额选项")
  public Mono<Result<List<PayAmountOptionRes>>> getPayAmountOptions(
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", key);
            put("type", "1");
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.PAY_AMOUNT_OPTIONS,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new PayAmountOptionRes(
              result.getLong("id"),
              result.getInteger("rechargeMoney"),
              result.getInteger("status"),
              result.getInteger("giftId"));
        });
  }

  @GetMapping(value = "/v1/types.do")
  @ApiOperation(value = "获取支付方式列表")
  public Mono<Result<List<PayWay>>> getPayWays(@ApiIgnore ServerWebExchange exchange) {
    DictionaryProperties.Pay payConfig = DictionaryProperties.get().getPay();

    List<PayWay> payWays;
    if (OsType.IOS.equals(getOsType(exchange))) {
      payWays =
          payConfig.getIosPayWays().stream().map(PayWay::valueOf).collect(Collectors.toList());
    } else {
      payWays =
          payConfig.getAndroidPayWays().stream().map(PayWay::valueOf).collect(Collectors.toList());
    }
    return Mono.just(Result.success(payWays));
  }
}
