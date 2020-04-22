package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.VipConfigItemRes;
import com.gaea.single.bridge.enums.VipType;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user/vip", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "VIP服务")
@Validated
@Slf4j
public class VipController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/config.do")
  @ApiOperation(value = "获取vip配置")
  public Mono<Result<List<VipConfigItemRes>>> getVipConfig(@ApiIgnore ServerWebExchange exchange) {
    return loboClient.postForm(
        exchange,
        LoboPathConst.GET_VIP_CONFIG,
        null,
        (obj) -> {
          JSONArray array = (JSONArray) obj;
          return array.stream()
              .filter(
                  item -> {
                    JSONObject result = (JSONObject) item;
                    // 过滤掉永久vip
                    return VipType.ofCode(result.getInteger("type")) != VipType.PERMANENT_VIP;
                  })
              .map(UserConverter.toVipConfigItemRes::convert)
              .collect(Collectors.toList());
        });
  }
}
