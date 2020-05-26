package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.media.CreateVideoOrderRes;
import com.gaea.single.bridge.dto.media.ValidateVideoOrderRes;
import com.gaea.single.bridge.dto.media.VideoEndInfoRes;
import com.gaea.single.bridge.dto.media.VideoShowItemRes;
import com.gaea.single.bridge.dto.user.LabelRes;
import com.gaea.single.bridge.enums.FollowStatus;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class VideoConverter {
  public static final Converter<Object, CreateVideoOrderRes> toCreateVideoOrderRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        CreateVideoOrderRes res = new CreateVideoOrderRes();
        res.setOrderTempId(result.getString("orderRedisId"));
        res.setCallerNickName(result.getString("callingNickName"));
        res.setCallerGradeUrl(result.getString("orderRedisId"));
        res.setCallerGrade(result.getInteger("callingGrade"));
        res.setCallerPortraitUrl(result.getString("callingPortrait"));
        res.setCallerShowId(result.getLong("callingUserShowId"));
        res.setCallerUserId(result.getLong("callingUserId"));
        res.setCalledNickName(result.getString("calledNickName"));
        res.setCalledGradeUrl(result.getString("calledGradeUrl"));
        res.setCalledGrade(result.getInteger("calledGrade"));
        res.setCalledPortraitUrl(result.getString("calledPortrait"));
        res.setCalledShowId(result.getLong("calledUserShowId"));
        res.setCalledUserId(result.getLong("calledUserId"));
        res.setPrice(result.getInteger("price"));
        res.setHeartTime(result.getInteger("heartTime"));

        return res;
      };

  public static final Converter<Object, ValidateVideoOrderRes> toValidateVideoOrderRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ValidateVideoOrderRes res = new ValidateVideoOrderRes();
        res.setOrderTempId(result.getString("orderRedisId"));
        res.setCallerNickName(result.getString("callingNickName"));
        res.setCallerGradeUrl(result.getString("orderRedisId"));
        res.setCallerGrade(result.getInteger("callingGrade"));
        res.setCallerPortraitUrl(result.getString("callingPortrait"));
        res.setCallerShowId(result.getLong("callingUserShowId"));
        res.setCallerUserId(result.getLong("callingUserId"));
        res.setCalledNickName(result.getString("calledNickName"));
        res.setCalledGradeUrl(result.getString("calledGradeUrl"));
        res.setCalledGrade(result.getInteger("calledGrade"));
        res.setCalledPortraitUrl(result.getString("calledPortrait"));
        res.setCalledShowId(result.getLong("calledUserShowId"));
        res.setCalledUserId(result.getLong("calledUserId"));
        res.setPrice(result.getInteger("price"));

        return res;
      };

  public static final Converter<Object, VideoEndInfoRes> toVideoEndInfoRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        VideoEndInfoRes res = new VideoEndInfoRes();
        res.setDurationTime(result.getInteger("callTimes"));
        res.setSpentDiamonds(result.getInteger("money"));
        res.setAnchor(getAnchor(result));
        res.setLabels(getLabels(result));
        res.setRecommendAnchors(getRecommendAnchors(result));

        return res;
      };

  private static VideoEndInfoRes.Anchor getAnchor(JSONObject result) {
    VideoEndInfoRes.Anchor anchor = new VideoEndInfoRes.Anchor();
    anchor.setUserId(result.getLong("userId"));
    anchor.setShowId(result.getString("showId"));
    anchor.setNickName(result.getString("nickName"));
    anchor.setPortraitUrl(result.getString("portarit"));
    anchor.setGrade(result.getInteger("grade"));
    anchor.setGradeIconUrl(result.getString("gradeIcon"));
    return anchor;
  }

  private static List<LabelRes> getLabels(JSONObject result) {
    JSONArray labels = result.getJSONArray("labels");
    if (labels != null) {
      return labels.stream()
          .map(
              obj -> {
                JSONObject label = (JSONObject) obj;
                return new LabelRes(
                    label.getLong("id"), label.getString("name"), label.getString("color"));
              })
          .collect(Collectors.toList());
    }
    return null;
  }

  private static List<VideoEndInfoRes.RecommendAnchor> getRecommendAnchors(JSONObject result) {
    JSONArray recommendAnchors = result.getJSONArray("labels");
    if (recommendAnchors != null) {
      return recommendAnchors.stream()
          .map(
              obj -> {
                JSONObject label = (JSONObject) obj;
                return new VideoEndInfoRes.RecommendAnchor(
                    label.getLong("id"), label.getString("nickName"), label.getString("portrait"));
              })
          .collect(Collectors.toList());
    }
    return null;
  }

  public static final Converter<Object, VideoShowItemRes> toVideoShowItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;

        VideoShowItemRes res = new VideoShowItemRes();
        res.setVideoId(result.getLong("id"));
        res.setCoverUrl(result.getString("coverUrl"));
        res.setVideoUrl(result.getString("videoUrl"));
        res.setIsPraise(LoboUtil.toBoolean(result.getInteger("isPriase")));
        res.setPraiseTimes(result.getLong("praiseTimes"));
        res.setGratuityMoney(result.getLong("rewardTotalMoney"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));

        VideoShowItemRes.UserInfo userInfo = new VideoShowItemRes.UserInfo();
        userInfo.setUserId(result.getLong("userId"));
        userInfo.setNickName(result.getString("nickName"));
        userInfo.setIntro(result.getString("intruduction"));
        userInfo.setPortraitUrl(result.getString("portrait"));
        userInfo.setGradeIcon(result.getString("gradeIcon"));
        userInfo.setFollowStatus(FollowStatus.ofCode(result.getInteger("followStatus")));

        res.setUserInfo(userInfo);

        return res;
      };
}
