package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.IncomeRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "账户服务")
@Validated
public class AccountController extends BaseController {
  @Autowired private LoboClient loboClient;

  @Autowired
  @Qualifier("loboOtherClient")
  private LoboClient loboOtherClient;

  @GetMapping(value = "/v1/balance.do")
  @ApiOperation(value = "获取账户余额")
  public Mono<Result<Long>> getAccountBalance(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "key");
          }
        };
    return loboClient.get(
        exchange, LoboPathConst.ACCOUNT_BALANCE, data, (result) -> Long.valueOf(result.toString()));
  }

  @GetMapping(value = "/v1/income.do")
  @ApiOperation(value = "获取个人收益")
  public Mono<Result<IncomeRes>> getIncome(@ApiIgnore ServerWebExchange exchange) {
    return loboOtherClient.postForm(
        exchange, LoboPathConst.INCOME, null, AccountConverter.toIncomeRes);
  }
}
