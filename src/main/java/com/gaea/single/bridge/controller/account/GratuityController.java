package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.AccountConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.GratuityGiftItemRes;
import com.gaea.single.bridge.enums.GiftType;
import com.gaea.single.bridge.core.error.ErrorCode;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
    value = "/account/gratuity",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "打赏服务")
@Validated
public class GratuityController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/gifts.net")
  @ApiOperation(value = "获取打赏礼物列表")
  public Mono<Result<List<GratuityGiftItemRes>>> getGratuityGiftList(
      @ApiIgnore ServerWebExchange exchange) {
    return loboClient
        .postFormForList(
            exchange,
            LoboPathConst.GRATUITY_GIFT_LIST,
            null,
            AccountConverter.toGratuityGiftItemRes)
        .map(
            result -> {
              if (result.getCode() == ErrorCode.SUCCESS.getCode()) {
                List<GratuityGiftItemRes> gifts =
                    result.getData().stream()
                        .filter(item -> item.getType() == GiftType.GENERAL)
                        .collect(Collectors.toList());
                result.setData(gifts);
              }
              return result;
            });
  }
}
