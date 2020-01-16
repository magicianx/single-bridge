package com.gaea.single.bridge.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.constant.LoboPathConst;
import com.gaea.single.bridge.controller.BaseContoller;
import com.gaea.single.bridge.dto.Result;
import com.gaea.single.bridge.dto.user.AnchorColumnRes;
import com.gaea.single.bridge.dto.user.AnchorItemRes;
import com.gaea.single.bridge.enums.AnchorOnlineStatus;
import com.gaea.single.bridge.support.lobo.LoboClient;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        exchange,
        LoboPathConst.ANCHOR_COLUMN_LIST,
        data,
        (result) -> {
          JSONArray array = result.getJSONArray("menuList");
          return array.stream()
              .sorted(
                  Comparator.comparingInt(item -> ((JSONObject) item).getInteger("orderNumber")))
              .map(
                  obj -> {
                    JSONObject item = ((JSONObject) obj);
                    return new AnchorColumnRes(item.getString("id"), item.getString("name"));
                  })
              .collect(Collectors.toList());
        });
  }

  @GetMapping(value = "/v1/list.net")
  @ApiOperation(value = "获取主播列表")
  public Mono<Result<List<AnchorItemRes>>> getAnchorList(
      @ApiParam(value = "栏目id", required = true) @NotBlank @RequestParam String columnId,
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
        exchange,
        LoboPathConst.ANCHOR_COLUMN_LIST,
        data,
        (result) -> {
          AnchorItemRes res = new AnchorItemRes();
          res.setUserId(result.getString("userId"));
          res.setNickName(result.getString("nickName"));
          res.setGrade(result.getInteger("grade"));
          res.setGradeIcon(result.getString("gradeIcon"));
          res.setOnlineStatus(AnchorOnlineStatus.ofCode(result.getInteger("status")));
          res.setSignature(result.getString("comment"));
          return res;
        });
  }
}
