package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
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
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping(value = "/v1/pay.do")
  @ApiOperation(value = "微信支付")
  public Mono<Result<WechatPayRes>> wechatPay(
      @Valid GetPaySignReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", Md5Utils.encrypt("hferfhhvudiewdweerrsdwddff"));
            put("configId", getOsType(exchange).getCode());
            put("appId", getAppId());
          }
        };
    return loboClient.get(
        exchange, LoboPathConst.WECHAT_PAY_SIGN, data, (obj) -> new WechatPayRes());
  }
}
