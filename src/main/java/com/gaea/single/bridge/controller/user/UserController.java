package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.error.ErrorCode;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
    value = "/user",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户服务")
@Validated
public class UserController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/info.do")
  //  @ApiOperation(value = "获取当前登录用户信息")
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
            put("packageName", req.getPackageName());
            put("accessToken", req.getAccessToken());
            put("userName", req.getNickName());
            put("version", getAppVersion(exchange));
            put("userMobile", req.getPhoneNum());
            put("password", req.getPassword());
            put("smsCode", req.getSmsCode());
          }
        };
    return loboClient
        .postForm(exchange, LoboPathConst.USER_LOGIN, data, UserConverter.toLoginRes)
        .flatMap(
            result -> {
              if (result.getCode() == ErrorCode.SUCCESS.getCode()) {
                LoginRes res = result.getData();
                exchange.getAttributes().put(CommonHeaderConst.SESSION, res.getSession());
                exchange.getAttributes().put(CommonHeaderConst.USER_ID, res.getId().toString());

                Mono<Result<UserProfileRes>> userProfileMono = getUserInfo(exchange);
                Mono<Result<JSONObject>> perfectInoMono =
                    loboClient.postForm(
                        exchange,
                        LoboPathConst.PERFECT_INFO,
                        null,
                        perfectResult -> (JSONObject) perfectResult);

                return Mono.zip(userProfileMono, perfectInoMono)
                    .map(
                        tuple2 -> {
                          Result<UserProfileRes> userProfile = tuple2.getT1();
                          Result<JSONObject> perfectInfo = tuple2.getT2();
                          if (userProfile.getCode() == ErrorCode.SUCCESS.getCode()) {
                            res.setIsPerfectBirthday(userProfile.getData().getIsPerfectBirthday());
                            res.setIsPerfectGender(userProfile.getData().getIsPerfectGender());
                            res.setBalance(userProfile.getData().getBalance());
                          }
                          if (perfectInfo.getCode() == ErrorCode.SUCCESS.getCode()) {
                            res.setIsVideoPerfect(
                                perfectInfo.getData().getInteger("isVideoPerfect") == 1);
                            res.setIsAlbumPerfect(
                                perfectInfo.getData().getInteger("isPhotoPerfect") == 1);
                          }
                          return result;
                        });
              }
              return Mono.just(Result.error(result.getCode(), result.getMessage()));
            });
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
    int key;
    int type;
    String paramName = req.getName();
    if (req.getName().equals("nickName")) {
      key = 1;
      type = 1;
    } else if (req.getName().equals("intro")) {
      key = 2;
      type = 2;
    } else if (req.getName().equals("gender")) {
      key = 3;
      type = 3;
      paramName = "sex";
    } else if (req.getName().equals("birthday")) {
      key = 4;
      type = 4;
    } else {
      return Mono.just(Result.error(ErrorCode.BAD_REQUEST));
    }

    Map<String, Object> data = new HashMap<>();
    data.put(paramName, req.getValue());
    data.put("type", type);
    data.put("key", key);
    return loboClient.postForm(exchange, LoboPathConst.EDIT_USER_INFO, data, null);
  }
}
