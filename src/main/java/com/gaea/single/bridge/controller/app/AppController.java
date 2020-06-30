package com.gaea.single.bridge.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.app.AppInfoRes;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.service.UserGreetService;
import com.gaea.single.bridge.service.UserService;
import com.gaea.single.bridge.util.LoboUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "应用服务")
@Validated
public class AppController extends BaseController {
  @Autowired
  @Qualifier("iosAuditClient")
  private LoboClient iosAuditClient;

  @Autowired private UserService userService;
  @Autowired private LoboClient loboClient;
  @Autowired private UserGreetService userGreetService;

  @GetMapping(value = "/v1/info.net")
  @ApiOperation(value = "获取app信息")
  public Mono<Result<AppInfoRes>> getAppInfo(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("version", getAppVersion(exchange));
            put("packageName", getPackageName(exchange));
            put("channelId", getChannelId(exchange));
            put("single", "single");
          }
        };

    boolean isAndroid = OsType.ANDROID == getOsType(exchange);
    String path =
        isAndroid ? LoboPathConst.CHECK_ANDROID_AUDIT_STATUS : LoboPathConst.CHECK_IOS_AUDIT_STATUS;

    LoboClient client = isAndroid ? loboClient : iosAuditClient; // ios使用的是测试环境的接口

    return client
        .postForm(
            exchange,
            path,
            data,
            (obj) -> {
              boolean isAuditPass = true;
              if (obj != null) {
                isAuditPass =
                    isAndroid
                        ? AuditStatus.ofCode((Integer) obj) == AuditStatus.PASS
                        : Optional.ofNullable(((JSONObject) obj).getInteger("auditStatus"))
                            .map(LoboUtil::toBoolean2)
                            .orElse(true);
              }

              DictionaryProperties.Lobo lobo = DictionaryProperties.get().getLobo();

              return new AppInfoRes(
                  getAppId(),
                  isAuditPass ? 1234567890 : 0,
                  DictionaryProperties.get().getAppStoreAudit().getUserColumnId(),
                  lobo.getUserSecretaryId(),
                  lobo.getAnchorSecretaryId());
            })
        // 由于android更新不需要重新登录，暂时在获取app信息时初始化用户，后面移到登录接口中
        .flatMap(
            res -> {
              Long userId = getUserId(exchange);
              if (ErrorCode.isSuccess(res.getCode()) && userId != null) {
                return userGreetService
                    .initGreetConfig(userId)
                    .then(Mono.defer(() -> userGreetService.addGreetUser(userId, false)))
                    .thenReturn(res);
              }
              return Mono.just(res);
            });
  }
}
