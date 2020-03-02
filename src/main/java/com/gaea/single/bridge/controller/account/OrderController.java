package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageReq;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.OrderDetailRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/order", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "订单服务")
@Validated
@Slf4j
public class OrderController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/records.do")
  @ApiOperation(value = "获取订单列表")
  public Mono<Result<PageRes<OrderDetailRes>>> getOrderRecords(
      @Valid PageReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = getPageData(req);
    data.put("userId", getUserId(exchange));
    return loboClient.postFormForPage(
        exchange,
        LoboPathConst.GET_ORDER_LIST,
        data,
        "billingList",
        AccountConverter.toOrderDetailRes);
  }
}
