package com.gaea.single.bridge.controller.account;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.GetPaySignReq;
import com.gaea.single.bridge.util.Md5Utils;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account/legend_shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "小羊商城支付服务")
@Validated
public class LegendShopController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/form.do")
  @ApiOperation(value = "获取小羊商城支付表单")
  public Mono<Result<String>> getWechatPaySign(
      @Valid GetPaySignReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", Md5Utils.encrypt("legendshopskupay" + req.getDiamonds()));
            put("type", req.getScene().getCode());
            put("configId", req.getOptionId()); // optionId 就是 skuId
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.GET_LEGEND_SHOP_PAY_FORM, data, (v) -> (String) v);
  }
}
