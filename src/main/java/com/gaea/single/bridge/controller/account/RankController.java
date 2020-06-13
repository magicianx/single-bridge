package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.RankMenuRes;
import com.gaea.single.bridge.dto.account.RankingRes;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author cludy */
@RestController
@RequestMapping(value = "/account/rank", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "排名服务")
@Validated
public class RankController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/menu.net")
  @ApiOperation(value = "获取排行榜菜单列表")
  public Mono<Result<List<RankMenuRes>>> getRankMenuList(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("appId", getAppId());
    data.put("userId", getUserId(exchange));

    return loboClient.getForList(
        exchange, LoboPathConst.GET_RANK_MENU_LIST, data, AccountConverter.toRankGroupRes);
  }

  @GetMapping(value = "/v1/rank.net")
  @ApiOperation(value = "获取排行榜用户列表")
  public Mono<Result<RankingRes>> getRankUserList(
      @ApiIgnore ServerWebExchange exchange,
      @ApiParam(value = "菜单id", required = true) @RequestParam("menuId") String menuId) {
    Map<String, Object> data = new HashMap<>();
    data.put("pageNo", 1);
    data.put("appId", getAppId());
    data.put("menuId", menuId);
    data.put("pageSize", 30);
    data.put("userId", getUserId(exchange));

    return loboClient
        .getForPage(
            exchange, LoboPathConst.GET_RANK_LIST, data, null, AccountConverter.toRankUserRes)
        .map(res -> Result.success(new RankingRes("消费5000钻石即可上榜", res.getData().getRecords())));
  }
}
