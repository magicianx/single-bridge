package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.GuildInviteInfoRes;
import com.gaea.single.bridge.dto.user.ProcessGuildInviteReq;
import com.gaea.single.bridge.enums.GuildInviteStatus;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

/**
 * 公会用户管理
 *
 * @author cludy
 */
@RestController
@RequestMapping(value = "/user/guild", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户公会服务")
@Validated
@Slf4j
public class GuildUserController extends BaseController {
  @Qualifier("bossClient")
  @Autowired
  private LoboClient bossClient;

  @GetMapping(value = "/v1/invite.do")
  @ApiOperation(value = "获取用户公会邀请信息")
  public Mono<Result<GuildInviteInfoRes>> getUserGrade(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        ImmutableMap.<String, Object>builder().put("userId", getUserId(exchange)).build();
    return bossClient.postForm(
        exchange,
        LoboPathConst.USER_GUILD_AUTH_INFO,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new GuildInviteInfoRes(
              result.getLong("userId"),
              result.getString("nickName"),
              result.getString("companyName"),
              GuildInviteStatus.ofCode(result.getInteger("comStatus")));
        });
  }

  @PostMapping(value = "/v1/invite/process.do")
  @ApiOperation(value = "处理公会邀请")
  public Mono<Result<Void>> processGuildInvite(
      @RequestBody @Valid ProcessGuildInviteReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        ImmutableMap.<String, Object>builder()
            .put("userId", getUserId(exchange))
            .put("status", req.getIsAgree() ? "1" : "3")
            .build();
    return bossClient.postForm(exchange, LoboPathConst.UPDATE_GUILD_AUTH_INFO, data, null);
  }
}
