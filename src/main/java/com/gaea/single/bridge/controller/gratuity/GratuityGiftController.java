package com.gaea.single.bridge.controller.gratuity;

import com.gaea.single.bridge.dto.gratuity.GratuityGiftRes;
import com.gaea.single.bridge.service.GratuityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;

/** @author cludy */
@RestController
@RequestMapping(value = "/gratuity/gift", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "礼物打赏服务")
@Validated
public class GratuityGiftController {
  @Autowired private GratuityService gratuityService;

  @GetMapping(value = "/v1/list.net")
  @ApiOperation(value = "获取主播被打赏礼物列表")
  public Mono<List<GratuityGiftRes>> getGratuityGifts(
      @ApiParam(value = "主播id", required = true) @NotNull @RequestParam("userId") Long userId) {
    return gratuityService.getGratuityGifts(userId);
  }
}
