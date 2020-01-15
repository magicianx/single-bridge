package com.gaea.single.bridge.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/v1")
@Api(tags = "平台服务")
public class UserController {
  @GetMapping("/data")
  @ApiOperation(value = "用户信息", tags = "aaa1")
  public void getBanners() {}
}
