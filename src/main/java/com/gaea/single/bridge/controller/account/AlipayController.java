package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.BindAlipayReq;
import com.gaea.single.bridge.dto.account.UnbindAlipayReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/alipay", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "支付宝服务")
@Validated
public class AlipayController extends BaseController {
  @Autowired private LoboClient loboClient;

  /** 这里应该存在安全性问题，手机号不该由前端传入，下个阶段删除手机号参数 */
  @PostMapping(value = "/v1/unbind.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "解绑支付宝账号")
  public Mono<Result<Object>> unbindAlipayAccount(
      @RequestBody @Valid UnbindAlipayReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userMobile", req.getPhoneNum());
            put("smsCode", req.getSmsCode());
            put("key", "------");
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.UNBIND_ALIPAY_ACCOUNT, data, null);
  }

  /** 这里应该存在安全性问题，手机号不该由前端传入，下个阶段删除手机号参数 */
  @PostMapping(value = "/v1/bind.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "绑定支付宝账号")
  public Mono<Result<Object>> bindAlipayAccount(
      @RequestBody @Valid BindAlipayReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("authCode", req.getAuthCode());
            put("key", "-----");
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.BIND_ALIPAY_ACCOUNT, data, null);
  }

  @GetMapping(value = "/v1/auth_sign.do")
  @ApiOperation(value = "获取支付宝授权签名")
  public Mono<Result<String>> getAlipayAuthSign(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "-----");
          }
        };
    return loboClient.get(exchange, LoboPathConst.ALIPAY_BIND_SIGN, data, (obj) -> (String) obj);
  }
}
