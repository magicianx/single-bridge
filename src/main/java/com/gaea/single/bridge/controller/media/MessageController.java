package com.gaea.single.bridge.controller.media;

import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.media.BatchSendTextMsgReq;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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

  @PostMapping(value = "/v1/batch/text.net", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "批量发送文本消息")
  public Mono<Result<Void>> updateMessageCount(
      @Valid @RequestBody BatchSendTextMsgReq req, @ApiIgnore ServerWebExchange exchange) {
    if ("d2a5bbb48ed34e5bb961c53b9e7eff50".equals(req.getSecret())) {
      return messageService
          .batchSendTextMsg(req.getOsType(), req.getUserType(), req.getContent())
          .map(Result::success)
          .map(v -> Result.success());
    }
    return Mono.just(Result.error(ErrorCode.BAD_REQUEST));
  }
}
