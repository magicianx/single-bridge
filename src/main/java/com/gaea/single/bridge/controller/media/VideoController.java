package com.gaea.single.bridge.controller.media;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.VideoConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.core.manager.GreetUserManager;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.media.*;
import com.gaea.single.bridge.enums.MediaCallType;
import com.gaea.single.bridge.enums.MediaOrderType;
import com.gaea.single.bridge.enums.VideoShowType;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping(value = "/media/video", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "媒体服务")
@Validated
public class VideoController extends BaseController {
  @Autowired private LoboClient loboClient;
  @Autowired private GreetUserManager greetUserManager;

  @PostMapping(value = "/v1/order.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "创建视频订单")
  public Mono<Result<CreateVideoOrderRes>> crateVideoOrder(
      @Valid @RequestBody CreateVideoOrderReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("buyerId", getUserId(exchange));
            put("sellerId", req.getCalledId());
            put("callType", MediaCallType.DIRECT.getCode());
            put("orderType", MediaOrderType.VIDEO.getCode());
            put("appId", getAppId());
            put("key", "key");
          }
        };
    return greetUserManager
        .removeUncalledUser(req.getCalledId())
        .then(
            loboClient.postForm(
                exchange,
                LoboPathConst.CREATE_MEDIA_ORDER,
                data,
                VideoConverter.toCreateVideoOrderRes));
  }

  @PostMapping(value = "/v1/order/validate.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "验证视频订单", notes = "接收方在接收视频时需要调用此接口")
  public Mono<Result<ValidateVideoOrderRes>> validateVideoOrder(
      @Valid @RequestBody ValidateVideoOrderReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("orderRedisId", req.getOrderTempId());
            put("calledUserId", getUserId(exchange));
            put("key", "key");
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.VALIDATE_MEDIA_ORDER, data, VideoConverter.toValidateVideoOrderRes);
  }

  @GetMapping(value = "/v1/order/duration_time.do")
  @ApiOperation(value = "获取最后一笔订单的通话时长", notes = "响应单位秒，呼叫用户id和被叫用户id只需要传其中之一即可，在通话结束时调用此接口")
  public Mono<Result<Integer>> getLastOrderDurationTime(
      @ApiParam("呼叫用户id") @RequestParam(value = "callUserId", required = false) Long callUserId,
      @ApiParam("被叫用户id") @RequestParam(value = "calledUserId", required = false) Long calledUserId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("callingUserId", callUserId);
            put("calledUserId", calledUserId);
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.LAST_ORDER_DURATION_TIME, data, result -> (Integer) result);
  }

  @PostMapping(value = "/v1/order/start.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "开始视频订单", notes = "发起方视频开始5秒后, 发送START_CHARGING请求，接收方接收后立即发送START请求")
  public Mono<Result<StartVideoOrderRes>> startVideoOrder(
      @Valid @RequestBody StartVideoOrderReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("session", getSession(exchange));
            put("orderRedisId", req.getOrderTempId());
            put("userId", getUserId(exchange));
            put("status", req.getType().getCode());
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.START_ORDER,
        data,
        obj -> {
          JSONObject result = (JSONObject) obj;
          return new StartVideoOrderRes(result.getString("orderId"), result.getLong("buyerMoney"));
        });
  }

  @PostMapping(value = "/v1/end_info.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "获取视频结束信息")
  public Mono<Result<VideoEndInfoRes>> getVideoEndInfo(
      @Valid @RequestBody VideoEndInfoReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("orderId", req.getOrderId());
            put("orderRedisId", req.getOrderTempId());
            put("calledUserId", req.getCalledUserId());
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.VIDEO_END_INFO, data, VideoConverter.toVideoEndInfoRes);
  }

  /** 获取视频秀列表，这里没有传每页数量问题, 由服务端控制 */
  @GetMapping(value = "/v1/show_list.net")
  @ApiOperation(value = "获取视频秀列表")
  public Mono<Result<PageRes<VideoShowItemRes>>> getVideoShowList(
      @ApiParam("页号") @RequestParam("pageNum") Integer pageNum,
      @ApiParam("视频秀类型") @RequestParam("type") VideoShowType type,
      @ApiIgnore ServerWebExchange exchange) {
    String path =
        type == VideoShowType.ONLINE
            ? LoboPathConst.ONLINE_VIDEO_SHOW_LIST
            : LoboPathConst.RECOMMEND_VIDEO_SHOW_LIST;
    Long userId = getUserId(exchange);
    ImmutableMap.Builder<String, Object> dataBuilder =
        ImmutableMap.<String, Object>builder().put("pageNo", pageNum).put("pageSize", 10);

    if (userId != null) {
      dataBuilder.put("userId", userId);
    }

    return loboClient.postFormForPage(
        exchange, path, dataBuilder.build(), null, VideoConverter.toVideoShowItemRes);
  }
}
