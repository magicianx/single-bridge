package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.GetPaySignReq;
import com.gaea.single.bridge.dto.account.WechatPayRes;
import com.gaea.single.bridge.util.Md5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/wechat", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "微信支付服务")
@Validated
public class WechatController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/pay_sign.do")
  @ApiOperation(value = "获取微信支付签名")
  public Mono<Result<WechatPayRes>> getWechatPaySign(
      @Valid GetPaySignReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", Md5Utils.encrypt("huoaquaweixinachongz" + req.getDiamonds()));
            put("type", req.getScene().getCode());
            put("money", req.getDiamonds());
            put("os", getOsType(exchange).getCode());
            put("version", getAppVersion(exchange));
            put("packageName", getPackageName(exchange));
          }
        };
    return loboClient.get(
        exchange, LoboPathConst.WECHAT_PAY_SIGN, data, AccountConverter.toWechatPayRes);
  }
}
