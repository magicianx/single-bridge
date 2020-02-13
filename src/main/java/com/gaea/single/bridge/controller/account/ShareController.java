package com.gaea.single.bridge.controller.account;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.ShareConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.*;
import com.gaea.single.bridge.enums.ShareWayType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/share", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "分享服务")
@Validated
public class ShareController extends BaseController {
  @Autowired
  @Qualifier("loboOtherClient")
  private LoboClient loboClient;

  @GetMapping(value = "/v1/ranking.do")
  @ApiOperation(value = "获取分享奖励排行榜")
  public Mono<Result<List<ShareRankingRes>>> getShareRanking(
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "123");
            put("pageNo", "1");
            put("pageSize", "10");
            put("total", "0");
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.SHARE_RANKING,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new ShareRankingRes(
              result.getLong("userId"),
              result.getString("nickName"),
              result.getString("portrait"),
              result.getLong("money"));
        });
  }

  @GetMapping(value = "/v1/info.do")
  @ApiOperation(value = "获取分销信息")
  public Mono<Result<ShareInfoRes>> getShareInfo(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "123");
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.SHARE_INFO, data, ShareConverter.toShareInfoRes);
  }

  @GetMapping(value = "/v1/reward_record.do")
  @ApiOperation(value = "获取分享奖励记录")
  public Mono<Result<PageRes<ShareRewardRecordRes>>> getShareRewardRecord(
      @Valid PageReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "123");
            put("pageNo", req.getPageNum());
            put("pageSize", req.getPageSize());
          }
        };
    return loboClient.postFormForPage(
        exchange, LoboPathConst.SHARE_REWARD_RECORD, data, ShareConverter.toShareRewardRecordRes);
  }

  @GetMapping(value = "/v1/invite_record.do")
  @ApiOperation(value = "获取分享邀请记录")
  public Mono<Result<PageRes<ShareInviteRecordRes>>> getShareInviteRecord(
      @Valid PageReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "123");
            put("pageNo", req.getPageNum());
            put("pageSize", req.getPageSize());
          }
        };
    return loboClient.postFormForPage(
        exchange, LoboPathConst.SHARE_INVITE_RECORD, data, ShareConverter.toShareInviteRecordRes);
  }

  /** 请求中钻石数量下个阶段删除 */
  @PostMapping(value = "/v1/withdraw.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分销提现")
  public Mono<Result<Object>> withdraw(
      @Valid @RequestBody WithdrawReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", "key");
            put("money", req.getDiamonds());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.SHARE_WITHDRAW, data, null);
  }

  @GetMapping(value = "/v1/ways.do")
  @ApiOperation(value = "获取分享方式")
  public Mono<Result<List<ShareWayRes>>> getShareWays(
      @RequestParam("inviteCode") @ApiParam(value = "邀请码", required = true) @NotBlank
          String inviteCode) {
    String url = String.format(DictionaryProperties.get().getLobo().getShareUrl(), inviteCode);
    String title = DictionaryProperties.get().getLobo().getShareTitle();
    String content = DictionaryProperties.get().getLobo().getShareContent();

    List<ShareWayRes> ways = new ArrayList<>();
    for (ShareWayType type : ShareWayType.values()) {
      ways.add(new ShareWayRes(type, title, content, url));
    }
    return Mono.just(Result.success(ways));
  }
}
