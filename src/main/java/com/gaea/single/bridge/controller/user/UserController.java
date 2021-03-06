package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.constant.DefaultSettingConstant;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.error.BusinessException;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.core.lobo.LoboCode;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.LoginType;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.service.*;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.JsonUtils;
import com.gaea.single.bridge.util.LoboUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户服务")
@Validated
@Slf4j
public class UserController extends BaseController {
  @Autowired private LoboClient loboClient;
  @Autowired private MessageService yxMessageService;
  @Autowired private UserSocialInfoService userRegInfoService;
  @Autowired private UserService userService;
  @Autowired private UserGreetService userGreetService;
  @Autowired private GratuityService gratuityService;

  @GetMapping(value = "/v1/columns.net")
  @ApiOperation(value = "获取用户栏目列表")
  public Mono<Result<List<UserColumnRes>>> getUserColumns(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("appId", getAppId());
    data.put("userId", getUserId(exchange));
    return loboClient.postForm(
        exchange, LoboPathConst.USER_COLUMN_LIST, data, UserConverter.toUserColumnResList);
  }

  @GetMapping(value = "/v1/list.net")
  @ApiOperation(value = "获取用户列表")
  public Mono<Result<PageRes<UserItemRes>>> getUserList(
      @ApiParam(value = "栏目id", required = true) @NotNull @RequestParam Long columnId,
      @Valid PageReq pageReq,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = getPageData(pageReq);
    data.put("appId", getAppId());
    data.put("menuId", columnId);

    return loboClient
        .postFormForPage(exchange, LoboPathConst.USER_LIST, data, null, UserConverter.toUserItemRes)
        .flatMap(
            res -> {
              if (!res.isSuccess()) {
                return Mono.just(res);
              }

              Mono[] setCityMonos =
                  res.getData().getRecords().stream()
                      .map(
                          user ->
                              userService
                                  .isEnablePosition(user.getUserId())
                                  .map(
                                      enable -> {
                                        if (!enable) {
                                          user.setCity(DefaultSettingConstant.UNKNOWN_POSITION);
                                        }
                                        return user;
                                      }))
                      .toArray(Mono[]::new);

              return Mono.when(setCityMonos).thenReturn(res);
            });
  }

  @GetMapping(value = "/v1/profile.net")
  @ApiOperation(value = "获取用户资料")
  public Mono<Result<UserProfileRes>> getUserProfile(
      @ApiParam(value = "用户id", required = true) @NotNull @RequestParam Long userId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("appId", getAppId());
    data.put("profileId", userId);

    Mono<Result<UserProfileRes>> profileMono =
        loboClient
            .postForm(exchange, LoboPathConst.USER_PROFILE, data, UserConverter.toUserProfileRes)
            .flatMap(
                result -> {
                  if (result.isSuccess()) {
                    return gratuityService
                        .getRecentGifts(userId)
                        .map(
                            giftIcons -> {
                              result.getData().setGiftIcons(giftIcons);
                              return result;
                            })
                        .flatMap(
                            r ->
                                userService
                                    .isEnablePosition(result.getData().getUserId())
                                    .map(
                                        enable -> {
                                          if (!enable) {
                                            result
                                                .getData()
                                                .setCity(DefaultSettingConstant.UNKNOWN_POSITION);
                                          }
                                          return r;
                                        }));
                  }
                  return Mono.just(result);
                });

    Mono<Result<List<AlbumItemRes>>> albumMono =
        loboClient.postFormForList(
            exchange, LoboPathConst.USER_ALBUM, data, UserConverter.toAlbumItemRes);
    return Mono.zip(
            profileMono,
            albumMono,
            (res1, res2) -> {
              if (LoboCode.isErrorCode(res1.getCode())) {
                throw new BusinessException(
                    res1.getCode(), LoboCode.getErrorMessage(res1.getCode()));
              }
              if (res1.getCode() == ErrorCode.INNER_ERROR.getCode()) {
                throw ErrorCode.INNER_ERROR.newBusinessException();
              }
              if (LoboCode.isErrorCode(res2.getCode())) {
                throw new BusinessException(
                    res1.getCode(), LoboCode.getErrorMessage(res1.getCode()));
              }
              if (res2.getCode() == ErrorCode.INNER_ERROR.getCode()) {
                throw ErrorCode.INNER_ERROR.newBusinessException();
              }

              res2.getData().forEach(album -> res1.getData().getPhotos().add(album.getImgUrl()));

              return res1;
            })
        .onErrorResume(
            ex -> {
              if (ex instanceof BusinessException) {
                return Mono.just(Result.error(((BusinessException) ex).getCode(), ex.getMessage()));
              }
              return Mono.just(
                  Result.error(
                      ErrorCode.INNER_ERROR.getCode(), ErrorCode.INNER_ERROR.getMessage()));
            });
  }

  @GetMapping(value = "/v1/info.do")
  @ApiOperation(value = "获取当前登录用户信息")
  public Mono<Result<UserInfoRes>> getUserInfo(@ApiIgnore ServerWebExchange exchange) {
    return loboClient
        .postForm(exchange, LoboPathConst.USER_INFO, null, UserConverter.toUserRes)
        .flatMap(
            res -> {
              if (ErrorCode.isSuccess(res.getCode())) {
                return yxMessageService
                    .getMessageCount(res.getData().getId())
                    .flatMap(
                        count -> {
                          res.getData().setMessageCount(count);
                          if (res.getData().getUserType() == UserType.ANCHOR) {
                            return userGreetService
                                .removeGreetUser(res.getData().getId())
                                .thenReturn(res);
                          }
                          return Mono.just(res);
                        });
              }
              return Mono.just(res);
            });
  }

  @GetMapping(value = "/v1/blacks.do")
  @ApiOperation(value = "获取黑名单列表")
  public Mono<Result<PageRes<BlackUserRes>>> getBlackList(
      @ApiIgnore ServerWebExchange exchange, @Valid PageReq pageReq) {
    return loboClient.getForPage(
        exchange,
        LoboPathConst.BLACK_LIST,
        getPageData(pageReq),
        null,
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
    Map<String, Object> data = new HashMap<>();
    data.put("blackId", userId);
    data.put("type", "2");
    return loboClient.postForm(exchange, LoboPathConst.REMOVE_BLACK_USER, data, null);
  }

  @PostMapping(value = "/v1/login.net", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "用户登录")
  public Mono<Result<LoginRes>> login(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody LoginReq req) {
    Mono<Result<LoginRes>> mono;
    if (req.getType() == LoginType.PHONE_DIRECT) {
      Map<String, Object> data = new HashMap<>();
      data.put("os", getOsType(exchange).getCode());
      data.put("appId", getAppId());
      data.put("channel", getChannelId(exchange));
      data.put("deviceNo", getDeviceNo(exchange));
      data.put("token", req.getAccessToken());

      mono = loboClient.postForm(exchange, LoboPathConst.ONE_LOGIN, data, UserConverter.toLoginRes);
    } else {
      replaceToPasswordLoginForAudit(req);

      Map<String, Object> data = new HashMap<>();
      data.put("type", req.getType().getCode());
      data.put("os", getOsType(exchange).getCode());
      data.put("openId", req.getOpenId());
      data.put("imageUrl", req.getPortraitUrl());
      data.put("appId", getAppId());
      data.put("channel", getChannelId(exchange));
      data.put("deviceNo", getDeviceNo(exchange));
      data.put("accessToken", req.getAccessToken());
      data.put("userName", req.getNickName());
      data.put("version", getAppVersion(exchange));
      data.put("userMobile", req.getPhoneNum());
      data.put("password", req.getPassword());
      data.put("smsCode", req.getSmsCode());

      mono =
          loboClient.postForm(exchange, LoboPathConst.USER_LOGIN, data, UserConverter.toLoginRes);
    }
    if (StringUtils.isNotBlank(req.getInviteCode())) {
      Map<String, Object> data = new HashMap<>();
      data.put("inviteCode", req.getInviteCode());
      data.put("key", "key");
      mono =
          mono.flatMap(
              (result) -> {
                if (ErrorCode.isSuccess(result.getCode())) {
                  log.info("登录成功，用户{}即将绑定邀请码: {}", result.getData().getId(), req.getInviteCode());
                  exchange
                      .getAttributes()
                      .put(CommonHeaderConst.USER_ID, result.getData().getId().toString());
                  exchange
                      .getAttributes()
                      .put(CommonHeaderConst.SESSION, result.getData().getSession());
                  return loboClient
                      .postForm(exchange, LoboPathConst.BIND_INVITE_CODE, data, null)
                      .map((r) -> result)
                      .onErrorResume((ex) -> Mono.just(result));
                }
                return Mono.just(result);
              });
    }

    return mono.flatMap(
        result -> {
          LoginRes res = result.getData();
          if (ErrorCode.isSuccess(result.getCode())) {
            return userGreetService
                .initGreetConfig(res.getId())
                .then(
                    Mono.defer(
                        () -> userGreetService.addGreetUser(res.getId(), res.getIsRegister())))
                .thenReturn(result);
          }
          return Mono.just(result);
        });
  }

  @PostMapping(value = "/v1/cancel.do")
  @ApiOperation(value = "用户注销")
  public Mono<Result<Object>> cancel() {
    return Mono.just(Result.success());
  }

  @PostMapping(value = "/v1/logout.do")
  @ApiOperation(value = "用户退出登录")
  public Mono<Result<LoginRes>> logout() {
    return Mono.just(Result.success());
  }

  @GetMapping(value = "/v1/album.do")
  @ApiOperation(value = "获取用户相册列表")
  public Mono<Result<List<AlbumItemRes>>> getUserAlbum(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("profileId", getUserId(exchange));
    return loboClient.postFormForList(
        exchange, LoboPathConst.USER_ALBUM, data, UserConverter.toAlbumItemRes);
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
    Map<String, Object> data = new HashMap<>();
    data.put("file", req.getImg());

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
      @ApiIgnore ServerWebExchange exchange, @Valid DeleteAlbumReq req) {
    Map<String, Object> url = new HashMap<>();
    url.put("url", req.getImgUrl());
    url.put("status", req.getStatus().getCode());

    String urls = JsonUtils.toJsonString(Collections.singletonList(url));
    Map<String, Object> data = new HashMap<>();
    data.put("urls", urls);
    return loboClient.postForm(exchange, LoboPathConst.DELETE_ALBUM_IMG, data, null);
  }

  @PostMapping(value = "/v1/info.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(name = "portrait", value = "头像", paramType = "form", dataType = "__file"))
  @ApiOperation(value = "编辑用户资料", notes = "只传值发生变化的字段，普通用户需要上传头像, 性别和生日不可重复修改，昵称和个人简介修改后需要进行审核")
  public Mono<Result<Object>> modifyUserInfo(
      @Valid UpdateUserReq req, @ApiIgnore ServerWebExchange exchange) {
    if (req.getBirthday() != null) {
      long year =
          ChronoUnit.YEARS.between(DateUtil.toLocalDate(req.getBirthday()), LocalDate.now());
      if (year < 18) {
        return Mono.error(ErrorCode.AGE_LESS_THAN_LIMIT.newBusinessException());
      }
    }
    Integer appVersion = getAppIntVersion(exchange);

    List<Integer> loboErrorCodes = Collections.singletonList(1005);
    if (req.getNickName() != null) {
      return callUpdateUserInfo(
              exchange, appVersion, 1, "nickName", req.getNickName(), loboErrorCodes)
          .flatMap(
              result -> {
                boolean auditing = false;
                if (LoboCode.isErrorCode(result.getCode())) {
                  return Mono.error(
                      new BusinessException(
                          result.getCode(), LoboCode.getErrorMessage(result.getCode())));
                }
                if (result.getCode() == ErrorCode.INNER_ERROR.getCode()) {
                  return Mono.error(ErrorCode.INNER_ERROR.newBusinessException());
                }
                if (result.getCode() == 1005) {
                  auditing = true;
                }
                List<Mono<Result<Object>>> monos =
                    getUpdateOtherInfoMonos(exchange, appVersion, req, loboErrorCodes);
                if (monos.isEmpty() && auditing) {
                  return Mono.error(ErrorCode.USER_INFO_AUDITING.newBusinessException());
                }
                return modifyUserOtherInfo(auditing, monos);
              });
    }
    return modifyUserOtherInfo(
        false, getUpdateOtherInfoMonos(exchange, appVersion, req, loboErrorCodes));
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

  @PostMapping(value = "/v1/address.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "上报用户位置")
  public Mono<Result<Void>> uploadUserAddress(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody UploadUserAddressReq req) {
    Map<String, Object> data = new HashMap<>();
    data.put("province", req.getProvince());
    data.put("city", req.getCity());
    data.put("latitude", req.getLatitude());
    data.put("longitude", req.getLongitude());
    data.put("key", getAppId());
    return loboClient.postForm(exchange, LoboPathConst.UPLOAD_USER_ADDRESS, data, null);
  }

  @GetMapping(value = "/v1/search.net")
  @ApiOperation(value = "搜索用户", notes = "可以搜索用户及主播")
  public Mono<Result<PageRes<SearchUserItemRes>>> searchUser(
      @ApiParam(value = "搜索关键字", required = true) @NotBlank @RequestParam String keyword,
      @Valid PageReq pageReq,
      @ApiIgnore ServerWebExchange exchange) {

    return userRegInfoService
        .findByShowId(getUserId(exchange), keyword)
        .map(
            u -> {
              List<SearchUserItemRes> users =
                  Collections.singletonList(
                      new SearchUserItemRes(
                          u.getUserId(), LoboUtil.getImageUrl(u.getPortrait()), u.getNickName()));

              PageRes<SearchUserItemRes> pageRes = PageRes.of(1, 1, 1, users);
              return Result.success(pageRes);
            })
        .defaultIfEmpty(Result.success(PageRes.empty()));
  }

  private List<Mono<Result<Object>>> getUpdateOtherInfoMonos(
      ServerWebExchange exchange,
      Integer appVersion,
      UpdateUserReq req,
      List<Integer> loboErrorCodes) {
    List<Mono<Result<Object>>> monos = new ArrayList<>();
    if (req.getIntro() != null) {
      monos.add(
          callUpdateUserInfo(exchange, appVersion, 2, "intro", req.getIntro(), loboErrorCodes));
    }
    if (req.getGender() != null) {
      monos.add(
          callUpdateUserInfo(exchange, appVersion, 3, "sex", req.getGender().getCode(), null));
    }
    if (req.getBirthday() != null) {
      monos.add(
          callUpdateUserInfo(
              exchange, appVersion, 4, "birthday", DateUtil.toLoboDate(req.getBirthday()), null));
    }
    if (req.getPortrait() != null) {
      monos.add(callUploadUserPortrait(exchange, req.getPortrait()));
    }
    return monos;
  }

  private Mono<Result<Object>> modifyUserOtherInfo(
      boolean nickNameAuditing, List<Mono<Result<Object>>> monos) {
    if (!monos.isEmpty()) {
      return Mono.zip(
              monos,
              (objs) -> {
                boolean auditing = nickNameAuditing;
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

  private Mono<Result<Object>> callUpdateUserInfo(
      ServerWebExchange exchange,
      Integer appVersion,
      int type,
      String name,
      Object value,
      List<Integer> loboErrorCodes) {
    Map<String, Object> data = new HashMap<>();
    data.put(name, value);
    data.put("type", type);
    data.put("key", type); // key和type的值相同
    data.put("appVersion", appVersion); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.EDIT_USER_INFO, data, null, loboErrorCodes);
  }

  private Mono<Result<Object>> callUploadUserPortrait(
      ServerWebExchange exchange, FilePart portrait) {
    Map<String, Object> data = new HashMap<>();
    data.put("file", portrait); // key和type的值相同
    return loboClient.postForm(exchange, LoboPathConst.UPLOAD_USER_PORTRAIT, data, url -> url);
  }

  /**
   * 如果是使用app store审核账号使用验证码登录, 替换为密码登录
   *
   * @param req {@link LoginReq}
   */
  private void replaceToPasswordLoginForAudit(LoginReq req) {
    DictionaryProperties.AppStoreAudit auditConfig = DictionaryProperties.get().getAppStoreAudit();
    if (req.getType() == LoginType.PHONE_SMS_CODE
        && auditConfig.getPhones().contains(req.getPhoneNum())) {
      log.info("使用应用审核手机号 {} 进行验证码登录，转换为密码登录", req.getPhoneNum());
      req.setType(LoginType.PHONE_PASSWORD);
      req.setPassword(auditConfig.getPassword());
    }
  }
}
