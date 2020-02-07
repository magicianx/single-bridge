package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.BusinessException;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.core.lobo.LoboCode;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.LoginType;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.JsonUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

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

  @PostMapping(value = "/v1/login.net", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "用户登录")
  public Mono<Result<LoginRes>> login(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody LoginReq req) {
    if (req.getType() == LoginType.PHONE_DIRECT) {
      Map<String, Object> data =
          new HashMap<String, Object>() {
            {
              put("os", getOsType(exchange).getCode());
              put("appId", getAppId());
              put("channel", getChannelId());
              put("deviceNo", getDeviceNo(exchange));
              put("token", req.getAccessToken());
            }
          };
      return loboClient.postForm(exchange, LoboPathConst.ONE_LOGIN, data, UserConverter.toLoginRes);
    } else {
      Map<String, Object> data =
          new HashMap<String, Object>() {
            {
              put("type", req.getType().getCode());
              put("os", getOsType(exchange).getCode());
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
      return loboClient.postForm(
          exchange, LoboPathConst.USER_LOGIN, data, UserConverter.toLoginRes);
    }
  }

  @GetMapping(value = "/v1/album.do")
  @ApiOperation(value = "获取用户相册列表")
  public Mono<Result<List<AlbumItemRes>>> getUserAlbum(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("profileId", getUserId(exchange));
          }
        };
    Mono<Result<AlbumItemRes>> portraitMono = this.getUserPortrait(exchange);
    Mono<Result<List<AlbumItemRes>>> albumMono =
        loboClient.postFormForList(
            exchange, LoboPathConst.USER_ALBUM, data, UserConverter.toAlbumItemRes);
    return Mono.zip(portraitMono, albumMono)
        .map(
            tuple2 -> {
              List<AlbumItemRes> items = new ArrayList<>();
              items.add(tuple2.getT1().getData());
              items.addAll(tuple2.getT2().getData());
              return Result.success(items);
            });
  }

  @PostMapping(value = "/v1/album.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "img",
          value = "图片",
          paramType = "form",
          dataType = "__file",
          required = true))
  @ApiOperation(value = "上传相册图片")
  public Mono<Result<List<AlbumItemRes>>> uploadAlbumImg(
      @Valid UploadAlbumReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("file", req.getImg());
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.UPLOAD_ALBUM_IMG,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          JSONArray albumItems = result.getJSONArray("album");
          return albumItems.stream()
              .map(
                  i -> {
                    JSONObject item = (JSONObject) i;
                    return new AlbumItemRes(
                        item.getString("url"),
                        AuditStatus.ofCode(item.getInteger("status")),
                        false);
                  })
              .collect(Collectors.toList());
        });
  }

  @DeleteMapping(value = "/v1/album.do")
  @ApiOperation(value = "删除相册图片")
  public Mono<Result<Object>> removeAlbumImg(
      @ApiIgnore ServerWebExchange exchange, @RequestBody @Valid DeleteAlbumReq req) {
    String urls =
        JsonUtils.toJsonString(
            Collections.singletonList(
                new HashMap<String, Object>() {
                  {
                    put("url", req.getImgUrl());
                    put("status", req.getStatus().getCode());
                  }
                }));
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("urls", urls);
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.DELETE_ALBUM_IMG, data, null);
  }

  /** 封面即用户头像 */
  @PostMapping(value = "/v1/album/cover.do")
  @ApiOperation(value = "设置相册封面", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<Result<Object>> setAlbumCover(
      @ApiIgnore ServerWebExchange exchange, @Valid SetAlbumCoverReq req)
      throws MalformedURLException {
    Mono<Result<Object>> removeAlbumMono = this.removeAlbumImg(exchange, req);
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("file", new UrlResource(req.getImgUrl()));
          }
        };
    Mono<Result<Object>> uploadMono =
        loboClient.postForm(exchange, LoboPathConst.UPLOAD_USER_PORTRAIT, data, (obj) -> null);
    return Mono.zip(removeAlbumMono, uploadMono).map((v) -> Result.success());
  }

  @PostMapping(value = "/v1/info.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(name = "portrait", value = "头像", paramType = "form", dataType = "__file"))
  @ApiOperation(value = "编辑用户资料", notes = "只传值发生变化的字段，普通用户需要上传头像, 性别和生日不可重复修改，昵称和个人简介修改后需要进行审核")
  public Mono<Result<Object>> modifyUserInfo(
      @Valid UpdateUserReq req, @ApiIgnore ServerWebExchange exchange) {
    List<Mono<Result<Object>>> monos = new ArrayList<>();
    List<Integer> loboErrorCodes = Collections.singletonList(1005);
    if (req.getNickName() != null) {
      monos.add(callUpdateUserInfo(exchange, 1, "nickName", req.getNickName(), loboErrorCodes));
    }
    if (req.getIntro() != null) {
      monos.add(callUpdateUserInfo(exchange, 2, "intro", req.getIntro(), loboErrorCodes));
    }
    if (req.getGender() != null) {
      monos.add(callUpdateUserInfo(exchange, 3, "sex", req.getGender().getCode(), null));
    }
    if (req.getBirthday() != null) {
      monos.add(
          callUpdateUserInfo(
              exchange, 4, "birthday", DateUtil.toLoboDate(req.getBirthday()), null));
    }
    if (req.getPortrait() != null) {
      monos.add(callUploadUserPortrait(exchange, req.getPortrait()));
    }
    if (!monos.isEmpty()) {
      return Mono.zip(
              monos,
              (objs) -> {
                boolean auditing = false;
                for (Object obj : objs) {
                  Result<Object> result = (Result<Object>) obj;
                  if (LoboCode.isErrorCode(result.getCode())) {
                    throw new BusinessException(
                        result.getCode(), LoboCode.getErrorMessage(result.getCode()));
                  }

                  if (result.getCode() == ErrorCode.INNER_ERROR.getCode()) {
                    throw ErrorCode.INNER_ERROR.newBusinessException();
                  }

                  if (!auditing && result.getCode() == 1005) {
                    auditing = true;
                  }
                }
                if (auditing) {
                  throw ErrorCode.USER_INFO_AUDITING.newBusinessException();
                }
                return new Object();
              })
          .map(Result::success)
          .onErrorResume(
              ex -> {
                if (ex instanceof BusinessException) {
                  return Mono.just(
                      Result.error(((BusinessException) ex).getCode(), ex.getMessage()));
                }
                return Mono.just(
                    Result.error(
                        ErrorCode.INNER_ERROR.getCode(), ErrorCode.INNER_ERROR.getMessage()));
              });
    }
    return Mono.just(Result.success());
  }

  @GetMapping(value = "/v1/portrait.do")
  @ApiOperation(value = "获取用户头像")
  public Mono<Result<AlbumItemRes>> getUserPortrait(@ApiIgnore ServerWebExchange exchange) {
    return loboClient.get(
        exchange,
        LoboPathConst.GET_USER_PORTRAIT,
        null,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new AlbumItemRes(
              result.getString("url"), AuditStatus.ofCode(result.getInteger("status")), true);
        });
  }

  private Mono<Result<Object>> callUpdateUserInfo(
      ServerWebExchange exchange,
      int type,
      String name,
      Object value,
      List<Integer> loboErrorCodes) {
    Map<String, Object> data = new HashMap<>();
    data.put(name, value);
    data.put("type", type);
    data.put("key", type); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.EDIT_USER_INFO, data, null, loboErrorCodes);
  }

  private Mono<Result<Object>> callUploadUserPortrait(
      ServerWebExchange exchange, FilePart portrait) {
    Map<String, Object> data = new HashMap<>();
    data.put("file", portrait); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.UPLOAD_USER_PORTRAIT, data, url -> url);
  }
}
