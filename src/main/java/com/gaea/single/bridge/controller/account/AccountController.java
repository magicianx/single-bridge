package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.IncomeRes;
import com.gaea.single.bridge.dto.account.WithdrawReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "账户服务")
@Validated
@Slf4j
public class AccountController extends BaseController {
  @Autowired private LoboClient loboClient;

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
    exchange.getRequest().getHeaders().forEach((k, v) -> log.info("请求头: {}:{}", k, v));
    return loboClient.postForm(exchange, LoboPathConst.INCOME, null, AccountConverter.toIncomeRes);
  }

  @PostMapping(value = "/v1/income_withdraw.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "主播收益提现")
  public Mono<Result<Object>> withdraw(
      @Valid @RequestBody WithdrawReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "key");
            put("money", req.getDiamonds());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.INCOME_WITHDRAW, data, null);
  }
}
