package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.FollowStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/follow", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户关注服务")
@Validated
@Slf4j
public class FollowController extends BaseController {
  @Autowired private LoboClient loboClient;

  @PostMapping(value = "/v1/add.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "添加关注")
  public Mono<Result<FollowRes>> follow(
      @Valid @RequestBody FollowReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("followId", req.getUserId());
            put("type", "2");
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.FOLLOW,
        data,
        obj -> new FollowRes(FollowStatus.ofCode((Integer) obj)));
  }

  @PostMapping(value = "/v1/cancel.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "取消关注")
  public Mono<Result<Object>> unfollow(
      @Valid @RequestBody UnfollowReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("followId", req.getUserId());
            put("type", "1");
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.UNFOLLOW, data, null);
  }

  @GetMapping(value = "/v1/followers.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "获取关注分页列表")
  public Mono<Result<PageRes<FollowItemRes>>> getFollowList(
      @Valid PageReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = getPageData(req);
    data.put("type", "1");
    return loboClient.getForPage(
        exchange, LoboPathConst.GET_FOLLOW_LIST, data, null, UserConverter.toFollowItemRes);
  }

  @GetMapping(value = "/v1/fans.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "获取粉丝分页列表")
  public Mono<Result<PageRes<FansItemRes>>> getFansList(
      @Valid PageReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = getPageData(req);
    data.put("type", "2");
    return loboClient.getForPage(
        exchange, LoboPathConst.GET_FANS_LIST, data, null, UserConverter.toFansItemRes);
  }
}
