package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户服务")
@Validated
public class UserController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/info.do")
  @ApiOperation(value = "获取当前登录用户信息")
  public Mono<Result<UserProfileRes>> getUserInfo(@ApiIgnore ServerWebExchange exchange) {
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
              result.getLong("userId"),
              result.getString("nickName"),
              result.getString("portrait"),
              result.getString("yunxinId"));
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

  @PostMapping(value = "/v1/login.net")
  @ApiOperation(value = "用户登录")
  public Mono<Result<LoginRes>> login(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody LoginReq req) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", req.getType().getCode());
            put("os", getOsType(exchange));
            put("openId", req.getOpenId());
            put("imageUrl", req.getPortraitUrl());
            put("appId", getAppId());
            put("channel", getChannelId());
            put("deviceNo", getDeviceNo(exchange));
            put("accessToken", req.getAccessToken());
            put("userName", req.getNickName());
            put("version", getAppVersion(exchange));
            put("userMobile", req.getPhoneNum());
            put("password", req.getPassword());
            put("smsCode", req.getSmsCode());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.USER_LOGIN, data, UserConverter.toLoginRes);
  }

  @GetMapping(value = "/v1/album.do")
  @ApiOperation(value = "获取用户相册")
  public Mono<Result<List<AlbumItemRes>>> getUserAlbum(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("profileId", getUserId(exchange));
          }
        };

    return loboClient.postFormForList(
        exchange, LoboPathConst.USER_ALBUM, data, UserConverter.toAlbumItemRes);
  }

  @PostMapping(value = "/v1/info.do")
  @ApiOperation(value = "编辑用户资料")
  public Mono<Result<Object>> modifyUserInfo(
      @Valid @RequestBody UpdateUserReq req, @ApiIgnore ServerWebExchange exchange) {
    List<Mono<Result<Object>>> monos = new ArrayList<>();
    if (req.getNickName() != null) {
      monos.add(callUpdateUserInfo(exchange, 1, "nickName", req.getNickName()));
    } else if (req.getIntro() != null) {
      monos.add(callUpdateUserInfo(exchange, 2, "intro", req.getIntro()));
    } else if (req.getGender() != null) {
      monos.add(callUpdateUserInfo(exchange, 3, "sex", req.getGender()));
    } else if (req.getBirthday() != null) {
      monos.add(callUpdateUserInfo(exchange, 4, "birthday", req.getBirthday()));
    }

    if (!monos.isEmpty()) {
      return Mono.zip(monos, (objs) -> objs).map(result -> Result.success());
    }
    return Mono.just(Result.success());
  }

  private Mono<Result<Object>> callUpdateUserInfo(
      ServerWebExchange exchange, int type, String name, String value) {
    Map<String, Object> data = new HashMap<>();
    data.put(name, value);
    data.put("type", type);
    data.put("key", type); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.EDIT_USER_INFO, data, null);
  }

  private Mono<Result<String>> callUpdateUserPortrait(
      ServerWebExchange exchange, FilePart portrait) {
    Map<String, Object> data = new HashMap<>();
    data.put("file", portrait); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.UPDATE_USER_PORTRAIT, data, null);
  }
}
