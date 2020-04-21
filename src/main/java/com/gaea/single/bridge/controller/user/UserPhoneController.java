package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.BindUserPhoneReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/phone", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户手机号服务")
@Validated
@Slf4j
public class UserPhoneController extends BaseController {
  @Autowired private LoboClient loboClient;

  @PostMapping(value = "/v1/bind.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "绑定手机号")
  public Mono<Result<Void>> bindPhone(
      @ApiIgnore ServerWebExchange exchange, @Valid @RequestBody BindUserPhoneReq req) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userMobile", req.getUserMobile());
            put("smsCode", req.getSmsCode());
            put("areaNo", ""); //  默认为86, single目前没有区号的概念，只能绑定中国手机号
            put("key", "key");
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.BIND_PHONE, data, null);
  }
}
