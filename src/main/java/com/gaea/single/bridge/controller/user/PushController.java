package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.SaveIOSPushKitReq;
import com.gaea.single.bridge.util.Md5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/user/push", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "推送服务")
@Validated
public class PushController extends BaseController {
  @Autowired private LoboClient loboClient;

  @PostMapping(value = "/v1/ios_push_kit.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "保存用户IOS PushKit信息")
  public Mono<Result<Object>> saveIOSPushKitAccount(
      @RequestBody @Valid SaveIOSPushKitReq req, @ApiIgnore ServerWebExchange exchange) {
    Long userId = getUserId(exchange);
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userId", userId);
            put("type", getChannelId());
            put("token", req.getToken());
            put("key", Md5Utils.encrypt(userId + req.getToken()));
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.SAVE_IOS_PUSH_KIT_ACCOUNT, data, null);
  }
}
