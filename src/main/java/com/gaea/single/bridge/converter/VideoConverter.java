package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.media.CreateVideoOrderRes;
import com.gaea.single.bridge.dto.media.ValidateVideoOrderRes;
import com.gaea.single.bridge.dto.media.VideoEndInfoRes;
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
        res.setCalledNickName(result.getString("calledNickName"));
        res.setCalledGradeUrl(result.getString("calledGradeUrl"));
        res.setCalledGrade(result.getInteger("calledGrade"));
        res.setCalledPortraitUrl(result.getString("calledPortrait"));
        res.setCalledShowId(result.getLong("calledUserShowId"));
        res.setPrice(result.getInteger("price"));
        res.setHeartTime(result.getInteger("heartTime"));

        return res;
      };

  public static final Converter<Object, ValidateVideoOrderRes> toValidateVideoOrderRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ValidateVideoOrderRes res = new ValidateVideoOrderRes();
        res.setCallerNickName(result.getString("callingNickName"));
        res.setCallerGradeUrl(result.getString("orderRedisId"));
        res.setCallerGrade(result.getInteger("callingGrade"));
        res.setCallerPortraitUrl(result.getString("callingPortrait"));
        res.setCallerShowId(result.getLong("callingUserShowId"));
        res.setCalledNickName(result.getString("calledNickName"));
        res.setCalledGradeUrl(result.getString("calledGradeUrl"));
        res.setCalledGrade(result.getInteger("calledGrade"));
        res.setCalledPortraitUrl(result.getString("calledPortrait"));
        res.setCalledShowId(result.getLong("calledUserShowId"));
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

  private static List<VideoEndInfoRes.Label> getLabels(JSONObject result) {
    JSONArray labels = result.getJSONArray("labels");
    if (labels != null) {
      return labels.stream()
          .map(
              obj -> {
                JSONObject label = (JSONObject) obj;
                return new VideoEndInfoRes.Label(
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
}