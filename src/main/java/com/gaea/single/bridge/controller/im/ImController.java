package com.gaea.single.bridge.controller.im;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseContoller;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/im", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "IM服务")
@Validated
public class ImController extends BaseContoller {
  @Autowired private LoboClient loboClient;

  @PostMapping(value = "/v1/order.net")
//  @ApiOperation(value = "创建IM订单")
  @Deprecated
  public Mono<Result<Object>> createImOrder(
      @ApiParam(value = "卖家id", required = true) @NotNull Long sellerId,
      @ApiParam(value = "买家id", required = true) @NotNull Long buyerId,
      @ApiParam(value = "是否卖家发送im消息，0买1卖，默认买家发送消息", required = true) @NotNull
          Integer sellerInitiative,
      @ApiIgnore ServerWebExchange exchange) {

    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("sellerId", sellerId);
            put("buyerId", buyerId);
            put("sellerInitiative", sellerInitiative);
            put("key", "key");
            put("appId", getAppId(exchange));
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.GRATUITY_GIFT_LIST, data, null);
  }
}
