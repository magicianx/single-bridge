package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.SwitchPositionStatusReq;
import com.gaea.single.bridge.service.UserService;
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

@RestController
@RequestMapping(value = "/user/position", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户位置服务")
@Validated
@Slf4j
public class UserPositionController extends BaseController {
  @Autowired private UserService userService;

  @PostMapping(value = "/v1/switch.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "切换定位功能状态")
  public Mono<Result<Void>> switchPositionStatus(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody SwitchPositionStatusReq req) {
    return userService
        .switchPositionStatus(getUserId(exchange), req.getIsEnable())
        .thenReturn(Result.success());
  }

  @GetMapping(value = "/v1/status.do")
  @ApiOperation(value = "获取定位功能状态")
  public Mono<Result<Boolean>> getPositionStatus(@ApiIgnore ServerWebExchange exchange) {
    return userService.isEnablePosition(getUserId(exchange)).map(Result::success);
  }
}
