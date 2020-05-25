package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.CommonHeaderConst;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.GetPaySignReq;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.util.Md5Utils;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/legend_shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "小羊商城支付服务")
@Validated
@Slf4j
public class LegendShopController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/form.do")
  @ApiOperation(value = "获取小羊商城支付表单")
  public Mono<Result<String>> getLegendShopPayForm(
      @Valid GetPaySignReq req, @ApiIgnore ServerWebExchange exchange) {
    if (StringUtils.isBlank(req.getSession())) {
      String session = exchange.getAttribute(CommonHeaderConst.SESSION);
      if (StringUtils.isBlank(session)) {
        return Mono.just(Result.error(ErrorCode.BAD_REQUEST));
      }
      exchange.getAttributes().put(CommonHeaderConst.SESSION, session);
    }

    if (StringUtils.isBlank(req.getUserId())) {
      String userId = exchange.getAttribute(CommonHeaderConst.USER_ID);
      if (StringUtils.isBlank(userId)) {
        return Mono.just(Result.error(ErrorCode.BAD_REQUEST));
      }
      exchange.getAttributes().put(CommonHeaderConst.USER_ID, userId);
    }

    String version =
        StringUtils.isBlank(req.getVersion()) ? req.getVersion() : getAppVersion(exchange);
    OsType osType = req.getOsType() == null ? req.getOsType() : getOsType(exchange);
    String packageName =
        StringUtils.isBlank(req.getPackageName()) ? req.getPackageName() : getPackageName(exchange);

    if (osType == null || StringUtils.isAnyBlank(version, packageName)) {
      return Mono.just(Result.error(ErrorCode.BAD_REQUEST));
    }

    Map<String, Object> data =
        ImmutableMap.<String, Object>builder()
            .put("key", Md5Utils.encrypt("legendshopskupay" + req.getOptionId()))
            .put("type", req.getScene().getCode())
            .put("configId", req.getOptionId())
            .put("version", version)
            .put("osType", osType.getCode())
            .put("packageName", packageName)
            .build();
    return loboClient.postForm(
        exchange, LoboPathConst.GET_LEGEND_SHOP_PAY_FORM, data, (v) -> (String) v);
  }
}
