package com.gaea.single.bridge.controller.media;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.MediaConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.media.CreateVideoOrderReq;
import com.gaea.single.bridge.dto.media.CreateVideoOrderRes;
import com.gaea.single.bridge.dto.media.ValidateVideoOrderReq;
import com.gaea.single.bridge.dto.media.ValidateVideoOrderRes;
import com.gaea.single.bridge.enums.MediaCallType;
import com.gaea.single.bridge.enums.MediaOrderType;
import com.gaea.single.bridge.error.ErrorCode;
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
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/media/video", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "媒体服务")
@Validated
public class VideoController extends BaseController {
  @Autowired private LoboClient loboClient;

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
            put("key", "key");
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.CREATE_MEDIA_ORDER, data, MediaConverter.toCreateMediaOrderRes);
  }

  @PostMapping(value = "/v1/order/validate.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "验证视频订单")
  public Mono<Result<ValidateVideoOrderRes>> validateVideoOrder(
      @Valid @RequestBody ValidateVideoOrderReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("orderRedisId", req.getOrderId());
            put("calledUserId", getUserId(exchange));
            put("key", "key");
          }
        };
    return loboClient.postForm(
        exchange,
        LoboPathConst.VALIDATE_MEDIA_ORDER,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          if (result.getInteger("type") == MediaOrderType.VIDEO.getCode()) {
            return MediaConverter.toValidateMediaOrderRes.convert(obj);
          }
          throw ErrorCode.INVALID_VIDEO_ORDER.newBusinessException();
        });
  }

  @GetMapping(value = "/v1/order/duration_time.do")
  @ApiOperation(value = "获取最后一笔订单的通话时长", notes = "响应单位秒，呼叫用户id和被叫用户id只需要传其中之一即可")
  public Mono<Result<Integer>> getLastOrderDurationTime(
      @ApiParam("呼叫用户id") @RequestParam(value = "callerId", required = false) Long callerId,
      @ApiParam("被叫用户id") @RequestParam(value = "calledId", required = false) Long calledId,
      @ApiParam("视频订单id") @RequestParam(value = "orderId") @NotBlank String orderId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("callingUserId", callerId);
            put("calledUserId", calledId);
            put("orderRedisId", orderId);
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.LAST_ORDER_DURATION_TIME, data, result -> (Integer) result);
  }
}
