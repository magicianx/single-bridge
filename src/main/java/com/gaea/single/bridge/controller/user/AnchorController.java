package com.gaea.single.bridge.controller.user;

import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseContoller;
import com.gaea.single.bridge.converter.AnchorConverter;
import com.gaea.single.bridge.core.lobo.LoboClient;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.ReportType;
import com.gaea.single.bridge.error.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/anchor", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "主播服务")
@Validated
public class AnchorController extends BaseContoller {
  @Autowired private LoboClient loboClient;

  @GetMapping(value = "/v1/columns.net")
  @ApiOperation(value = "获取主播栏目列表")
  public Mono<Result<List<AnchorColumnRes>>> getAnchorColumns(
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("appId", getAppId(exchange));
            put("userId", getUserId(exchange));
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.ANCHOR_COLUMN_LIST, data, AnchorConverter.toAnchorColumnResList);
  }

  @GetMapping(value = "/v1/list.net")
  @ApiOperation(value = "获取主播列表")
  public Mono<Result<List<AnchorItemRes>>> getAnchorList(
      @ApiParam(value = "栏目id", required = true) @NotNull @RequestParam Long columnId,
      @ApiParam(value = "第几页, 从1开始", required = true) @NotNull @RequestParam Integer pageNum,
      @ApiParam(value = "每页条数", required = true) @NotNull @RequestParam Integer pageSize,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("appId", getAppId(exchange));
            put("menuId", columnId);
            put("pageNo", pageNum);
            put("pageSize", pageSize);
          }
        };
    return loboClient.postFormForList(
        exchange, LoboPathConst.ANCHOR_LIST, data, AnchorConverter.toAnchorItemRes);
  }

  @GetMapping(value = "/v1/profile.net")
  @ApiOperation(value = "获取主播资料")
  public Mono<Result<AnchorProfileRes>> getAnchorProfile(
      @ApiParam(value = "主播id", required = true) @NotNull @RequestParam Long userId,
      @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("appId", getAppId(exchange));
            put("profileId", userId);
          }
        };
    return loboClient.postForm(
        exchange, LoboPathConst.ANCHOR_PROFILE, data, AnchorConverter.toAnchorProfileRes);
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
        (result ->
            new ReportSelectItemRes(result.getString("reason"), result.getString("isChecked"))));
  }

  @PostMapping(value = "/v1/report.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation(value = "举报或拉黑主播")
  public Mono<Result<?>> reportAnchor(
      @Valid ReportAnchorReq req, @ApiIgnore ServerWebExchange exchange) {
    Map<String, Object> data =
        new HashMap<String, Object>() {
          {
            put("complainUserId", req.getReportUserId());
            put("complainedUserId", req.getReportedUserId());
            put("reason", req.getReason());
            put("key", " ---- ");
            put("file", req.getImg());
          }
        };

    // 举报主播
    return loboClient
        .postForm(exchange, LoboPathConst.REPORT_ANCHOR, data, null)
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
}
