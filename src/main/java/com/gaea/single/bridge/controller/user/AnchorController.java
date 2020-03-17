package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseController;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.ReportType;
import com.gaea.single.bridge.error.ErrorCode;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/anchor", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "主播服务")
@Validated
public class AnchorController extends BaseController {
  @Autowired private LoboClient loboClient;
  @Autowired private ServiceProperties serviceProperties;

  private FileSystemResource resource;

  @PostConstruct
  public void init() {
    resource = new FileSystemResource(serviceProperties.getReportImgPath());
  }

  @PostMapping(value = "/v1/auth.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation(value = "主播认证")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "portrait", value = "头像", paramType = "form", dataType = "__file"),
    @ApiImplicitParam(name = "video", value = "认证视频", paramType = "form", dataType = "__file")
  })
  public Mono<Result<UserProfileRes>> anchorAuth(
      @Valid AnchorAuthReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("portraitFile", req.getPortrait());
            put("videoFile", req.getVideo());
          }
        };
    String path = String.format(LoboPathConst.ANCHOR_AUTH, req.getNumber());
    return loboClient.postForm(exchange, path, data, null);
  }

  @GetMapping(value = "/v1/report_items.do")
  @ApiOperation(value = "获取举报选项列表")
  public Mono<Result<List<ReportSelectItemRes>>> getReportSelectItems(
      @ApiParam(value = "举报类型", required = true) @RequestParam @NotNull ReportType type,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("type", type.getCode());
          }
        };
    return loboClient.postFormForList(
        exchange,
        LoboPathConst.REPORT_SELECT_ITEMS,
        data,
        (obj -> {
          JSONObject result = (JSONObject) obj;
          return new ReportSelectItemRes(
              result.getString("reason"), result.getBoolean("isChecked"));
        }));
  }

  @PostMapping(value = "/v1/report.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "举报或拉黑主播")
  public Mono<Result<?>> reportAnchor(
      @Valid @RequestBody ReportAnchorReq req, @ApiIgnore ServerWebExchange exchange) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    HttpEntity<FileSystemResource> entity = new HttpEntity<>(resource, headers);

    Map<String, Object> params =
        new HashMap<String, Object>() {
          {
            put("complainUserId", getUserId(exchange));
            put("complainedUserId", req.getReportedUserId());
            put("reason", req.getReason());
            put("key", "----");
          }
        };

    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("file", entity);
          }
        };

    // 举报主播
    return loboClient
        .postForm(exchange, LoboPathConst.REPORT_ANCHOR, params, data, null)
        .flatMap(
            result -> {
              if (req.getIsPullBack() && result.getCode() == ErrorCode.SUCCESS.getCode()) {
                Map<String, Object> pullBackData =
                    new HashMap<String, Object>() {
                      {
                        put("blackId", req.getReportedUserId());
                        put("type", "1"); // 1 拉黑，2取消拉黑
                      }
                    };
                // 拉黑主播
                return loboClient.postForm(
                    exchange, LoboPathConst.PULL_BACK_ANCHOR, pullBackData, null);
              }
              return Mono.just(result);
            });
  }

  @GetMapping(value = "/v1/random_label.do")
  @ApiOperation(value = "获取主播随机标签列表")
  public Mono<Result<List<LabelRes>>> getRandomLabels(
      @RequestParam("anchorId") @ApiParam(value = "主播id", required = true) Long anchorId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userId", anchorId);
          }
        };
    return loboClient.getForList(
        exchange,
        LoboPathConst.ANCHOR_RANDOM_LABELS,
        data,
        (obj) -> {
          JSONObject result = (JSONObject) obj;
          return new LabelRes(
              result.getLong("id"), result.getString("name"), result.getString("color"));
        });
  }

  @PostMapping(value = "/v1/label.do", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "添加主播标签")
  public Mono<Result<Object>> addAnchorLabel(
      @Valid @RequestBody AddAnchorLabelReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("userId", req.getAnchorId());
            put("labelId", req.getLabelId());
          }
        };
    return loboClient.postForm(exchange, LoboPathConst.ADD_ANCHOR_LABEL, data, null);
  }
}
