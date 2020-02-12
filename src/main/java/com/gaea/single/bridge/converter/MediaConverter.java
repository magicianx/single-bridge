package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.media.CreateVideoOrderRes;
import com.gaea.single.bridge.dto.media.ValidateVideoOrderRes;
import org.springframework.core.convert.converter.Converter;

public class MediaConverter {
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
}
