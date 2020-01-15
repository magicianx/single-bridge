package com.gaea.single.bridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/api-docs")
public class ApiController {
  @GetMapping("/global")
  public String getApiGlobalInfo() {
    return "{}";
  }
}
