package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.converter.UserConverter;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user/video", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "用户视频服务")
@Validated
@Slf4j
public class UserVideoController extends BaseController {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/list.net")
  @ApiOperation(value = "获取视频列表")
  public Mono<Result<List<UserVideoItemRes>>> getVideos(
      @ApiParam("用户id, 获取当前登录用户的视频列表不需要传此参数") @RequestParam(required = false) Long userId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    if (userId == null) {
      Long currentUserId = getUserId(exchange);
      if (currentUserId == null) {
        return Mono.error(ErrorCode.BAD_REQUEST.newBusinessException("缺少userId参数"));
      }
      data.put("userId", currentUserId);
      data.put("type", "1");
    } else {
      data.put("userId", userId);
      data.put("type", "2");
    }

    data.put("viewUserId", getUserId(exchange));
    return loboClient.postFormForList(
        exchange, LoboPathConst.GET_VIDEO_LIST, data, UserConverter.toUserVideoItemRes);
  }

  @PostMapping(value = "/v1/delete.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "删除视频")
  public Mono<Result<Object>> deleteVideo(
      @RequestBody @Valid @NotEmpty List<DeleteVideoReq> req,
      @ApiIgnore ServerWebExchange exchange) {
    String obj =
        JsonUtils.toJsonString(
            req.stream()
                .map(
                    video -> {
                      Map<String, Object> item = new HashMap<>();
                      item.put("unid", video.getCloudFileId());
                      item.put("status", video.getAuditStatus().getCode());
                      return item;
                    })
                .collect(Collectors.toList()));
    Map<String, Object> data = new HashMap<>();
    data.put("obj", obj);
    return loboClient.postForm(exchange, LoboPathConst.DELETE_VIDEO, data, null);
  }

  @PostMapping(value = "/v1/praise.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "视频点赞")
  public Mono<Result<Object>> praiseVideo(
      @RequestBody @Valid PraiseVideoReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("videoUnId", req.getId());
    return loboClient.postForm(exchange, LoboPathConst.PRAISE_VIDEO, data, null);
  }

  @GetMapping(value = "/v1/sign.do")
  @ApiOperation(value = "获取上传视频签名")
  public Mono<Result<String>> getUploadSignature(@ApiIgnore ServerWebExchange exchange) {
    return loboClient.postForm(
        exchange, LoboPathConst.GET_UPLOAD_VIDEO_SIGN, null, obj -> (String) obj);
  }

  @PostMapping(value = "/v1/upload.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "上传视频")
  public Mono<Result<String>> uploadVideo(
      @RequestBody @Valid UploadVideoReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("unid", req.getCloudFileId());
    data.put("coverUrl", req.getCoverUrl());
    data.put("videoUrl", req.getVideoUrl());
    data.put("key", "----");
    return loboClient.postForm(exchange, LoboPathConst.UPLOAD_VIDEO, data, null);
  }

  @PostMapping(value = "/v1/cover.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "设置封面视频")
  public Mono<Result<String>> setCoverVideo(
      @RequestBody @Valid SetCoverVideoReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data = new HashMap<>();
    data.put("isCover", req.getIsCover() ? "1" : "2");
    data.put("id", req.getVideoId());
    data.put("userId", getUserId(exchange));

    return loboClient.postForm(exchange, LoboPathConst.SET_COVER_VIDEO, data, null);
  }
}
