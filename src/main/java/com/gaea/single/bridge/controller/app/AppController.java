package com.gaea.single.bridge.controller.app;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.app.AppInfoRes;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.OsType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
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
  public Mono<Result<AppInfoRes>> getAppInfo(
      @ApiIgnore ServerWebExchange exchange,
      @RequestParam("osType") @ApiParam(value = "系统类型", required = true) OsType osType) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("version", getAppVersion(exchange));
            put("packageName", getPacketName(exchange));
            put("channelId", getChannelId(exchange));
          }
        };

    String path =
        OsType.ANDROID == osType
            ? LoboPathConst.CHECK_ANDROID_AUDIT_STATUS
            : LoboPathConst.CHECK_IOS_AUDIT_STATUS;

    return loboClient.postForm(
        exchange,
        path,
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
