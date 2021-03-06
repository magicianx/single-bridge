package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.BindAlipayReq;
import com.gaea.single.bridge.dto.account.GetPaySignReq;
import com.gaea.single.bridge.dto.account.ManualBindAlipayReq;
import com.gaea.single.bridge.dto.account.UnbindAlipayReq;
import com.gaea.single.bridge.util.Md5Utils;
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
    Map<String, Object> data = new HashMap<>();
    data.put("userMobile", req.getPhoneNum());
    data.put("smsCode", req.getSmsCode());
    data.put("key", "------");
    return loboClient.postForm(exchange, LoboPathConst.UNBIND_ALIPAY_ACCOUNT, data, null);
  }

  /** 这里应该存在安全性问题，手机号不该由前端传入，下个阶段删除手机号参数 */
  @PostMapping(value = "/v1/bind.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "绑定支付宝账号")
  public Mono<Result<Object>> bindAlipayAccount(
      @RequestBody @Valid BindAlipayReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("authCode", req.getAuthCode());
    data.put("key", "-----");
    return loboClient.postForm(exchange, LoboPathConst.BIND_ALIPAY_ACCOUNT, data, null);
  }

  @GetMapping(value = "/v1/auth_sign.do")
  @ApiOperation(value = "获取支付宝授权签名")
  public Mono<Result<String>> getAlipayAuthSign(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("key", "-----");
    return loboClient.get(exchange, LoboPathConst.ALIPAY_AUTH_SIGN, data, (obj) -> (String) obj);
  }

  /** id先传过来，二阶段使用 */
  @GetMapping(value = "/v1/pay_sign.do")
  @ApiOperation(value = "获取支付宝支付签名")
  public Mono<Result<String>> getAlipayPaySign(
      @Valid GetPaySignReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("money", req.getDiamonds());
    data.put("os", getOsType(exchange).getCode());
    data.put("packageName", getPackageName(exchange));
    data.put("type", req.getScene().getCode());
    data.put("version", getAppVersion(exchange));
    data.put("key", Md5Utils.encrypt("huoaquazhifubaoachongz" + req.getDiamonds()));
    data.put("configId", req.getOptionId()); // vip充值时需要传vip支付配追id, 钱包充值不需要传

    return loboClient.get(exchange, LoboPathConst.ALIPAY_PAY_SIGN, data, (obj) -> (String) obj);
  }

  @PostMapping(value = "/v1/manual_bind.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "手动绑定支付宝账号")
  public Mono<Result<Object>> manualBindAlipayAccount(
      @RequestBody @Valid ManualBindAlipayReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("alipayId", req.getAlipayAccount());
    data.put("key", "-----");
    
    return loboClient.postForm(exchange, LoboPathConst.MANUAL_BIND_ALIPAY_ACCOUNT, data, null);
  }
}
