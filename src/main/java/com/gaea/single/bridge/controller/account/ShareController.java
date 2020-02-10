package com.gaea.single.bridge.controller.account;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.ShareConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.ShareInfoRes;
import com.gaea.single.bridge.dto.account.ShareRankingRes;
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
}
