package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.ViewRecordItemRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.Map;

/** @author cludy */
@RestController
@RequestMapping(value = "/user/view_record", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户查看记录服务")
@Validated
@Slf4j
public class UserViewRecordController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/all.do")
  @ApiOperation(value = "谁看过我(全部)")
  public Mono<Result<PageRes<ViewRecordItemRes>>> getAllViewRecords(
      @ApiParam(value = "第几页, 从1开始", required = true) @RequestParam("pageNum") @NotNull
          Integer pageNum,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = getPageData(pageNum, 10);

    return loboClient.postFormForPage(
        exchange, LoboPathConst.VIEW_RECORD_LIST, data, null, UserConverter.toViewRecordItemRes);
  }
}
