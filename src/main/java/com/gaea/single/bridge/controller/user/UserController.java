package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.BlackUserRes;
import com.gaea.single.bridge.dto.user.LoginReq;
import com.gaea.single.bridge.dto.user.LoginRes;
import com.gaea.single.bridge.dto.user.UserRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户服务")
@Validated
public class UserController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/info.do")
  @ApiOperation(value = "获取当前登录用户信息")
  public Mono<Result<UserRes>> getUserInfo(@ApiIgnore ServerWebExchange exchange) {
    return loboClient.postForm(exchange, LoboPathConst.USER_INFO, null, UserConverter.toUserRes);
  }

  @GetMapping(value = "/v1/blacks.do")
  @ApiOperation(value = "获取黑名单列表")
  public Mono<Result<PageRes<BlackUserRes>>> getBlackList(
      @ApiIgnore ServerWebExchange exchange, @Valid PageReq pageReq) {
    return loboClient.getForPage(
        exchange,
        LoboPathConst.BLACK_LIST,
        getPageData(pageReq),
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new BlackUserRes(
              result.getLong("userId"), result.getString("nickName"), result.getString("portrait"));
        });
  }

  @DeleteMapping(value = "/v1/black.do")
  @ApiOperation(value = "移除黑名单用户")
  public Mono<Result<PageRes<BlackUserRes>>> removeBlackUser(
      @ApiIgnore ServerWebExchange exchange,
      @ApiParam(value = "用户id", required = true) @RequestParam("userId") @NotNull Long userId) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("blackId", userId);
            put("type", "2");
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.REMOVE_BLACK_USER, data, null);
  }

  @PostMapping(value = "/v1/login.do")
  @ApiOperation(value = "用户登录")
  public Mono<Result<LoginRes>> login(
      @ApiIgnore ServerWebExchange exchange, @RequestBody @Valid LoginReq req) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", req.getType().getCode());
            put("os", req.getOs().getCode());
            put("openId", req.getOpenId());
            put("imageUrl", req.getPortraitUrl());
            put("appId", getAppId());
            put("channel", getChannelId());
            put("deviceNo", req.getDeviceNo());
            put("packageName", req.getPackageName());
            put("accessToken", req.getAccessToken());
            put("userName", req.getNickName());
            put("version", req.getNickName()); // @TODO
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.USER_LOGIN,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new LoginRes(result.getString("userId"), result.getString("session"));
        });
  }
}
