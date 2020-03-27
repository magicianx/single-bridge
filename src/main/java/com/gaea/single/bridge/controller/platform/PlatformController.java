package com.gaea.single.bridge.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.PlatformConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.platform.BannerRes;
import com.gaea.single.bridge.dto.platform.CheckVersionRes;
import com.gaea.single.bridge.dto.platform.DpPopupAdvertRes;
import com.gaea.single.bridge.dto.platform.SendSmsReq;
import com.gaea.single.bridge.enums.BannerType;
import com.gaea.single.bridge.enums.DeviceType;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.util.LoboUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "平台服务")
@Validated
public class PlatformController extends BaseController {
  @Autowired private LoboClient loboClient;

  @Autowired
  @Qualifier("iosAuditClient")
  private LoboClient iosAuditClient;

  @GetMapping(value = "/v1/banners.net")
  @ApiOperation(value = "获取广告列表")
  public Mono<Result<List<BannerRes>>> getBanners(
      @ApiParam(value = "deviceType", required = true) @NotNull @RequestParam("deviceType")
          DeviceType deviceType,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("deviceType", deviceType.getCode());
            put("type", 2);
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.BANNER_LIST,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new BannerRes(
              result.getString("imgUrl"),
              result.getString("linkToUrl"),
              result.getString("title"),
              BannerType.INNER);
        });
  }

  @PostMapping(value = "/v1/sms_code.net", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "发送验证码")
  public Mono<Result<Object>> sendSmsCode(
      @Valid @RequestBody SendSmsReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", req.getType().getCode());
            put("userMobile", req.getPhoneNum());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.SEND_SMS_CODE, data, null);
  }

  @GetMapping(value = "/v1/check_version.net")
  @ApiOperation(value = "检查版本号")
  public Mono<Result<CheckVersionRes>> checkVersion(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", getOsType(exchange).getCode());
            put("packageName", getPackageName(exchange));
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.CHECK_VERSION, data, PlatformConverter.toCheckVersionRes);
  }

  @GetMapping(value = "/v1/dp/popup_advert.net")
  @ApiOperation(value = "获取引流包弹窗广告地址")
  public Mono<Result<DpPopupAdvertRes>> getPopupAdvert(@ApiIgnore ServerWebExchange exchange) {
    if (getOsType(exchange) == OsType.IOS) {
      Map<String, Object> data =
          new HashMap<String, Object>() {
            {
              put("version", getAppVersion(exchange));
              put("packageName", getPackageName(exchange));
              put("single", "single");
            }
          };

      return iosAuditClient.postForm(
          exchange,
          LoboPathConst.CHECK_IOS_AUDIT_STATUS,
          data,
          (obj) -> {
            boolean isAuditPass = !LoboUtil.toBoolean(((JSONObject) obj).getInteger("auditStatus"));
            if (isAuditPass) {
              DictionaryProperties.DrainagePackage drainagePackage =
                  DictionaryProperties.get().getDrainagePackage();
              return new DpPopupAdvertRes(
                  drainagePackage.getAdvertImgUrl(), drainagePackage.getDownloadUrl());
            }
            return null;
          });
    }

    return Mono.error(ErrorCode.BAD_REQUEST.newBusinessException());
  }
}
