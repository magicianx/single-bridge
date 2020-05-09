package com.gaea.single.bridge.controller.user;

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
@RequestMapping(value = "/user/bp", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "埋点支持服务")
@Validated
public class BPSupportController extends BaseController {
  @Autowired private BPSupportService bpSupportService;

  @GetMapping(value = "/v1/first_recharge.net")
  @ApiOperation(value = "用户是否为第一次充值")
  public Mono<Result<Boolean>> isFirstRecharge(@ApiIgnore ServerWebExchange exchange) {
    return bpSupportService
        .isFirstRecharge(getUserId(exchange), getChannelId(exchange))
        .map(Result::success);
  }

  @GetMapping(value = "/v1/first_open_vip.net")
  @ApiOperation(value = "用户是否第一次开通vip")
  public Mono<Result<Boolean>> isFirstOpenVip(@ApiIgnore ServerWebExchange exchange) {
    return bpSupportService
        .isFirstOpenVip(getUserId(exchange), getChannelId(exchange))
        .map(Result::success);
  }

  @GetMapping(value = "/v1/today_register_recharge.net")
  @ApiOperation(value = "用户是否为当日注册并且首次充值")
  public Mono<Result<Boolean>> isTodayRegisterAndFirstRecharge(
      @ApiIgnore ServerWebExchange exchange) {
    return bpSupportService
        .isTodayRegisterAndFirstRecharge(getUserId(exchange), getChannelId(exchange))
        .map(Result::success);
  }
}
