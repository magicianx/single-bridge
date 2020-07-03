package com.gaea.single.bridge.controller.account;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.account.PayAmountOptionRes;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.OsType;
import com.gaea.single.bridge.enums.PayWay;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/account/pay", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "支付服务")
@Validated
public class PayController extends BaseController {
  @Autowired private LoboClient loboClient;
  private final String key = Md5Utils.encrypt("huoquchongzhijinelieb");

  @GetMapping(value = "/v1/options.do")
  @ApiOperation(value = "获取充值金额选项")
  public Mono<Result<List<PayAmountOptionRes>>> getPayAmountOptions(
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("key", key);
            put("type", "1");
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.PAY_AMOUNT_OPTIONS,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new PayAmountOptionRes(
              result.getLong("id"),
              result.getInteger("rechargeMoney"),
              result.getInteger("status"),
              result.getInteger("giftId"));
        });
  }

  @GetMapping(value = "/v1/types.do")
  @ApiOperation(value = "获取支付方式列表")
  public Mono<Result<List<PayWay>>> getPayWays(@ApiIgnore ServerWebExchange exchange) {
    DictionaryProperties.Pay payConfig = DictionaryProperties.get().getPay();

    // 测试人员展示所有支付方式，方便测试
    if (getUserId(exchange) == 1781298) {
      return Mono.just(Result.success(Arrays.asList(PayWay.ALIPAY, PayWay.LEGEND_SHOP_PAY)));
    } else {
      if (OsType.IOS.equals(getOsType(exchange))) {
        List<PayWay> payWays =
            payConfig.getIosPayWays().stream().map(PayWay::valueOf).collect(Collectors.toList());
        return Mono.just(Result.success(payWays));
      } else {
        return getAndroidAuditStatus(exchange)
            .map(
                auditPass ->
                    payConfig.getAndroidPayWays().stream()
                        .map(PayWay::valueOf)
                        // 安卓审核中不展示小羊商城支付
                        .filter(payWay -> payWay == PayWay.LEGEND_SHOP_PAY ? auditPass : true)
                        .collect(Collectors.toList()))
            .map(Result::success);
      }
    }
  }

  /**
   * 获取安卓审核状态, true: 审核通过 false: 审核未通过
   *
   * @param exchange {@link ServerWebExchange}
   * @return {@link Mono<Boolean>}
   */
  private Mono<Boolean> getAndroidAuditStatus(ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("version", getAppVersion(exchange));
    data.put("packageName", getPackageName(exchange));
    data.put("channelId", getChannelId(exchange));
    data.put("single", "single");

    return loboClient
        .postForm(
            exchange,
            LoboPathConst.CHECK_ANDROID_AUDIT_STATUS,
            data,
            (obj) -> obj == null || AuditStatus.ofCode((Integer) obj) == AuditStatus.PASS)
        .flatMap(
            r -> {
              if (r.isSuccess()) {
                return Mono.just(r.getData());
              }
              return Mono.error(ErrorCode.INNER_ERROR.newBusinessException());
            });
  }
}
