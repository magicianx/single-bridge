package com.gaea.single.bridge.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.app.AppInfoRes;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.OsType;
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

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "应用服务")
@Validated
public class AppController extends BaseController {
  @Autowired
  @Qualifier("appAuditClient")
  private LoboClient loboClient;

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

    return loboClient.postForm(
        exchange,
        path,
        data,
        (obj) -> {
          boolean isAuditPass = true;
          if (obj != null) {
            isAuditPass =
                isAndroid
                    ? AuditStatus.ofCode((Integer) obj) == AuditStatus.PASS
                    : !LoboUtil.toBoolean(((JSONObject) obj).getInteger("auditStatus"));
          }
          return new AppInfoRes(
              "1", isAuditPass ? 1234567890 : 0); // appId前端用于登录socket, 先用1，lobo处理后再改为5
        });
  }
}
