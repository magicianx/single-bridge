package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.user.AnchorColumnRes;
import com.gaea.single.bridge.dto.user.AnchorItemRes;
import com.gaea.single.bridge.dto.user.AnchorProfileRes;
import com.gaea.single.bridge.dto.user.LabelRes;
import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnchorConverter {
  public static final Converter<Object, List<AnchorColumnRes>> toAnchorColumnResList =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        JSONArray array = result.getJSONArray("menuList");
        return array.stream()
            .sorted(Comparator.comparingInt(item -> ((JSONObject) item).getInteger("orderNumber")))
            .map(
                i -> {
                  JSONObject item = ((JSONObject) i);
                  return new AnchorColumnRes(item.getLong("id"), item.getString("name"));
                })
            .collect(Collectors.toList());
      };

  public static final Converter<Object, AnchorItemRes> toAnchorItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        AnchorItemRes res = new AnchorItemRes();
        res.setUserId(result.getLong("userId"));
        res.setNickName(result.getString("nickName"));
        res.setGrade(result.getInteger("grade"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        res.setSignature(result.getString("comment"));
        res.setCoverUrl(result.getString("cover"));
        res.setPrice(result.getInteger("price"));
        return res;
      };

  public static final Converter<Object, AnchorProfileRes> toAnchorProfileRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;

        AnchorProfileRes res = new AnchorProfileRes();
        res.setUserId(result.getLong("id"));
        res.setShowId(result.getString("showId"));
        res.setYunXinId(result.getString("yunxinId"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("portrait"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setPrice(result.getInteger("price"));
        res.setGender(GenderType.ofCode(result.getInteger("sex")));
        res.setAge(result.getInteger("age"));
        res.setCity(
            StringUtils.isNotBlank(result.getString("address"))
                ? result.getString("address")
                : "火星");
        res.setIntro(result.getString("intro"));

        List<String> photos = new ArrayList<>();
        if (result.getJSONArray("photos") != null) {
          result.getJSONArray("photos").forEach(photo -> photos.add((String) photo));
        }
        if (result.getJSONArray("album") != null) {
          result.getJSONArray("album").forEach(photo -> photos.add((String) photo));
        }
        res.setPhotos(photos);

        Optional.ofNullable(result.getJSONArray("labels"))
            .map(
                labels ->
                    labels.stream()
                        .map(
                            l -> {
                              JSONObject label = (JSONObject) l;
                              return new LabelRes(
                                  label.getLong("id"),
                                  label.getString("name"),
                                  label.getString("color"));
                            })
                        .collect(Collectors.toList()))
            .ifPresent(res::setLabels);
        Optional.ofNullable(result.getJSONArray("giftIcons"))
            .map(icons -> icons.stream().map(icon -> (String) icon).collect(Collectors.toList()))
            .ifPresent(res::setGiftIcons);
        return res;
      };
}
