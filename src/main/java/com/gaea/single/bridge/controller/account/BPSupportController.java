package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.service.BPSupportService;
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

@RestController
@RequestMapping(value = "/account/bp", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "埋点支持服务")
@Validated
public class BPSupportController extends BaseController {
  @Autowired private BPSupportService bpSupportService;

  @GetMapping(value = "/v1/first_recharge.do")
  @ApiOperation(value = "用户是否为第一次充值(用于支付前判断)")
  public Mono<Result<Boolean>> isFirstRecharge(@ApiIgnore ServerWebExchange exchange) {
    return bpSupportService.isFirstRecharge(getUserId(exchange)).map(Result::success);
  }

  @GetMapping(value = "/v1/first_open_vip.do")
  @ApiOperation(value = "用户是否第一次开通vip(用于支付前判断)")
  public Mono<Result<Boolean>> isFirstOpenVip(@ApiIgnore ServerWebExchange exchange) {
    return bpSupportService.isFirstOpenVip(getUserId(exchange)).map(Result::success);
  }

  @GetMapping(value = "/v1/today_register_recharge.do")
  @ApiOperation(value = "用户是否为当日注册并且首次充值")
  public Mono<Result<Boolean>> isTodayRegisterAndFirstRecharge(
      @ApiIgnore ServerWebExchange exchange) {
    return bpSupportService
        .isTodayRegisterAndFirstRecharge(getUserId(exchange))
        .map(Result::success);
  }
}
