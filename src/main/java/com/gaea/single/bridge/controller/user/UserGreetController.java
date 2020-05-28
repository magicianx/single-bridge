package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.AddCustomGreetMessageReq;
import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.UseSystemGreetMessageReq;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import com.gaea.single.bridge.service.UserGreetService;
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
import javax.validation.constraints.NotNull;

/** @author cludy */
@RestController
@RequestMapping(value = "/user/greet", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户打招呼服务")
@Validated
@Slf4j
public class UserGreetController extends BaseController {
  @Autowired private UserGreetService userGreetService;

  @GetMapping(value = "/v1/config.do")
  @ApiOperation(value = "获取用户打招呼配置")
  public Mono<Result<UserGreetConfigRes>> getGreetConfig(@ApiIgnore ServerWebExchange exchange) {
    return userGreetService.getGreetConfig(getUserId(exchange)).map(Result::success);
  }

  @PostMapping(value = "/v1/message/add.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("添加自定义打招呼消息")
  public Mono<Result<GreetMessageRes>> addCustomGreetMessage(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody AddCustomGreetMessageReq req) {
    return userGreetService
        .addCustomGreetMessage(getUserId(exchange), req.getMessage())
        .map(Result::success);
  }

  @PostMapping(value = "/v1/message/delete.do")
  @ApiOperation("删除自定义打招呼消息")
  public Mono<Result<Void>> deleteCustomMessage(
      @ApiIgnore ServerWebExchange exchange, @RequestParam("messageId") @NotNull String messageId) {
    return userGreetService
        .deleteCustomMessage(getUserId(exchange), messageId)
        .thenReturn(Result.success());
  }

  @GetMapping(value = "/v1/enable.do")
  @ApiOperation("获取打招呼开启状态")
  public Mono<Result<Boolean>> isEnableGreet(@ApiIgnore ServerWebExchange exchange) {
    return userGreetService.isEnableGreet(getUserId(exchange)).map(Result::success);
  }

  @PostMapping(value = "/v1/message/use.do")
  @ApiOperation("使用系统打招呼消息")
  public Mono<Result<Void>> useSystemGreetMessage(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody UseSystemGreetMessageReq req) {
    return userGreetService
        .useSystemGreetMessage(getUserId(exchange), req.getMessageId(), req.getIsUse())
        .thenReturn(Result.success());
  }
}
