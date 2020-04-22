package com.gaea.single.bridge.controller.media;

import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/media/message", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "消息服务")
@Validated
public class MessageController extends BaseController {
  @Autowired private MessageService messageService;

  @PostMapping(value = "/v1/count/update.do")
  @ApiOperation(value = "更新消息数量")
  public Mono<Result<Integer>> updateMessageCount(@ApiIgnore ServerWebExchange exchange) {
    return messageService.decrMessageCount(getUserId(exchange)).map(Result::success);
  }
}
