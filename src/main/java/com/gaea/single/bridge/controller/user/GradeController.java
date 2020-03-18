package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.UserGradeRes;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/grade", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户等级服务")
@Validated
@Slf4j
public class GradeController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/info.do")
  @ApiOperation(value = "获取用户等级信息", tags = "getUserGrade")
  public Mono<Result<UserGradeRes>> getUserGrade(@ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userId", getUserId(exchange));
          }
        };
    return loboClient.get(
        exchange, LoboPathConst.GET_USER_GRADE, data, UserConverter.toUserGradeRes);
  }
}
