package com.gaea.single.bridge.controller.app;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.app.AppInfoRes;
import com.gaea.single.bridge.enums.AuditStatus;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "应用服务")
@Validated
public class AppController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/info.net")
  @ApiOperation(value = "获取app信息")
  public Mono<Result<AppInfoRes>> getAppInfo(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("version", getAppVersion(exchange));
            put("packageName", getPacketName(exchange));
            put("channelId", getChannelId(exchange));
          }
        };

    return loboClient.postForm(
        exchange,
        LoboPathConst.CHECK_APP_AUDIT_STATUS,
        data,
        (obj) -> {
          boolean isAuditPass = true;
          if (obj != null) {
            Integer result = (Integer) obj;
            isAuditPass = AuditStatus.ofCode(result) == AuditStatus.PASS;
          }
          return new AppInfoRes(isAuditPass);
        });
  }
}
