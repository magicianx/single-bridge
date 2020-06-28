package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.SetUserRemarkReq;
import com.gaea.single.bridge.dto.user.UserRemarkRes;
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
import java.util.List;
import java.util.Map;

/** @author cludy */
@RestController
@RequestMapping(value = "/user/remark", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户备注服务")
@Validated
@Slf4j
public class UserRemarkController {
  @Autowired private LoboClient loboClient;

  @PostMapping(value = "/v1/set.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "设置用户备注")
  public Mono<Result<Void>> setUserRemark(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody SetUserRemarkReq req) {
    Map<String, Object> data = new HashMap<>();
    data.put("remarksUserId", req.getUserId());
    data.put("remarks", req.getRemark());
    return loboClient.postForm(exchange, LoboPathConst.SET_USER_REMARK, data, null);
  }

  @GetMapping(value = "/v1/list.do")
  @ApiOperation(value = "获取用户备注列表")
  public Mono<Result<List<UserRemarkRes>>> getUserRemarkList(
      @ApiIgnore ServerWebExchange exchange) {
    return loboClient.postFormForList(
        exchange, LoboPathConst.GET_USER_REMARK_LIST, null, UserConverter.toUserRemarkRes);
  }
}
