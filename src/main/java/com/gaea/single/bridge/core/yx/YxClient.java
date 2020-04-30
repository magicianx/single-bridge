package com.gaea.single.bridge.core.yx;

import com.gaea.single.bridge.constant.YxCcidConstant;
import com.gaea.single.bridge.constant.YxPathConst;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.error.ErrorCode;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.JsonUtils;
import com.gaea.single.bridge.util.Sha1Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class YxClient {
  private final WebClient webClient;
  private final String appKey;
  private final String appSecret;

  public YxClient(String host, String appKey, String appSecret) {
    this.webClient = WebClient.builder().baseUrl(host).build();
    this.appKey = appKey;
    this.appSecret = appSecret;
  }

  /**
   * 批量发送文本消息
   *
   * @param userType 用户类型
   * @param ccids 接受消息用户的云信id
   * @param content 消息内容
   * @return 发送结果
   */
  public Mono<Void> sendBatchTextMsg(UserType userType, List<String> ccids, String content) {
    String curTime = String.valueOf(DateUtil.getNowMilliseconds() / 1000L);
    String nonce = getNonce();
    String checkSum = Sha1Utils.digest(appSecret + nonce + curTime);

    Map<String, String> body =
        new HashMap<String, String>() {
          {
            put("msg", content);
          }
        };

    Map<String, Boolean> options =
        new HashMap<String, Boolean>() {
          {
            put("roam", false); // 是否需要漫游
            put("history", false); // 是否存云端历史
            put("sendersync", false); // 是否需要发送方多端同步
            put("route", false); // 是否需要抄送第三方
          }
        };

    MultiValueMap<String, Object> data =
        new LinkedMultiValueMap<String, Object>() {
          {
            add(
                "fromAccid",
                UserType.ANCHOR == userType
                    ? YxCcidConstant.ANCHOR_SECRETARY_CCID
                    : YxCcidConstant.USER_SECRETARY_CCID);
            add("toAccids", JsonUtils.toJsonString(ccids));
            add("type", "0");
            add("body", JsonUtils.toJsonString(body));
            add("pushcontent", content);
            add("option", JsonUtils.toJsonString(options));
          }
        };

    return webClient
        .post()
        .uri(YxPathConst.BATCH_SEND_MESSAGE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("AppKey", appKey)
        .header("Nonce", nonce)
        .header("CurTime", curTime)
        .header("CheckSum", checkSum)
        .body(BodyInserters.fromMultipartData(data))
        .retrieve()
        .bodyToMono(YxResult.class)
        .flatMap(
            r -> {
              if (!r.isSuccess()) {
                return Mono.error(ErrorCode.INNER_ERROR.newBusinessException());
              }
              return Mono.empty();
            });
  }

  private String getNonce() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
