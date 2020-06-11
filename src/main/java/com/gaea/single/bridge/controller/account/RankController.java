package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.RankMenuRes;
import com.gaea.single.bridge.dto.account.RankUserRes;
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
import java.util.List;
import java.util.Map;

/** @author cludy */
@RestController
@RequestMapping(value = "/account/rank", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "排名服务")
@Validated
public class RankController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/menu.do")
  @ApiOperation(value = "获取排行榜菜单列表")
  public Mono<Result<List<RankMenuRes>>> getRankMenuList(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("appId", getAppId());
    data.put("userId", getUserId(exchange));

    return loboClient.getForList(
        exchange, LoboPathConst.GET_RANK_MENU_LIST, data, AccountConverter.toRankGroupRes);
  }

  @GetMapping(value = "/v1/rank.do")
  @ApiOperation(value = "获取排行榜用户列表")
  public Mono<Result<PageRes<RankUserRes>>> getRankUserList(
      @ApiIgnore ServerWebExchange exchange, Integer pageNo, String menuId) {
    Map<String, Object> data = new HashMap<>();
    data.put("pageNo", pageNo);
    data.put("appId", getAppId());
    data.put("menuId", menuId);
    data.put("pageSize", menuId);
    data.put("userId", getUserId(exchange));

    return loboClient.getForPage(
        exchange, LoboPathConst.GET_RANK_LIST, data, null, AccountConverter.toRankUserRes);
  }
}
