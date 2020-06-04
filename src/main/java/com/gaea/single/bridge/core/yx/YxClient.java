package com.gaea.single.bridge.core.yx;

import com.gaea.single.bridge.config.ServiceProperties;
import com.gaea.single.bridge.constant.YxPathConst;
import com.gaea.single.bridge.core.error.ErrorCode;
import com.gaea.single.bridge.core.manager.UserManager;
import com.gaea.single.bridge.entity.mysql.UserSocialInfo;
import com.gaea.single.bridge.enums.AnchorAuthStatus;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.repository.mysql.UserSocialInfoRepository;
import com.gaea.single.bridge.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class YxClient {
  @Autowired private ServiceProperties serviceProperties;
  @Autowired private UserSocialInfoRepository userSocialInfoRepository;
  @Autowired private UserManager userManager;

  private WebClient webClient;
  private String appKey;
  private String appSecret;

  @PostConstruct
  public void init() {
    ServiceProperties.Yx yx = serviceProperties.getYx();
    this.webClient = WebClient.builder().baseUrl(yx.getHost()).build();
    this.appKey = yx.getAppKey();
    this.appSecret = yx.getAppSecret();
  }

  /**
   * 发送文本消息
   *
   * @param fromYunXinId 发送者云信id
   * @param fromSocialInfo 发送着信息
   * @param toYunXinIds 接受消息的云信id
   * @param content 消息内容
   * @return {@link Mono<Void>}
   */
  public Mono<Void> sendMessage(
      String fromYunXinId,
      UserSocialInfo fromSocialInfo,
      List<String> toYunXinIds,
      String content) {
    String curTime = String.valueOf(DateUtil.getNowMilliseconds() / 1000L);
    String nonce = StringUtil.uuid();
    String checkSum = Sha1Utils.digest(appSecret + nonce + curTime);

    return getData(fromYunXinId, fromSocialInfo, toYunXinIds, content)
        .flatMap(
            data ->
                webClient
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
                            log.error(
                                "{} 批量推送云信文本消息 {} 失败: 数量 {}",
                                fromYunXinId,
                                content,
                                toYunXinIds.size());
                            return Mono.error(ErrorCode.INNER_ERROR.newBusinessException());
                          }
                          log.error(
                              "{} 批量推送云信文本消息 {} 成功: 数量 {}",
                              fromYunXinId,
                              content,
                              toYunXinIds.size());
                          return Mono.empty();
                        }));
  }

  private Mono<MultiValueMap<String, Object>> getData(
      String fromYunXinId,
      UserSocialInfo fromSocialInfo,
      List<String> toYunXinIds,
      String content) {
    return userManager
        .isVip(fromSocialInfo.getUserId())
        .map(
            isVip -> {
              Map<String, String> body = new HashMap<>();
              body.put("msg", content);

              Map<String, Boolean> options = new HashMap<>();
              // 是否需要漫游
              options.put("roam", false);
              // 是否存云端历史
              options.put("history", false);
              // 是否需要发送方多端同步
              options.put("sendersync", true);
              // 是否需要抄送第三方
              options.put("route", false);

              Map<String, Object> ext = new HashMap<>();
              ext.put("nickname", fromSocialInfo.getNickName());
              ext.put("headurl", LoboUtil.getImageUrl(fromSocialInfo.getPortrait()));
              ext.put("netid", fromYunXinId);
              ext.put("userid", fromSocialInfo.getUserId());
              ext.put(
                  "userType",
                  AnchorAuthStatus.ofCode(fromSocialInfo.getIsVideoAudit()).isAuditPass()
                      ? UserType.ANCHOR.getCode()
                      : UserType.GENERAL_USER.getCode());
              ext.put("isVip", LoboUtil.toInteger(isVip));

              MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
              data.add("fromAccid", fromYunXinId);
              data.add("toAccids", JsonUtils.toJsonString(toYunXinIds));
              data.add("type", "0");
              data.add("body", JsonUtils.toJsonString(body));
              data.add("pushcontent", content);
              data.add("option", JsonUtils.toJsonString(options));
              data.add("ext", JsonUtils.toJsonString(ext));

              return data;
            });
  }
}
