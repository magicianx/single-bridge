package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.user.AnchorColumnRes;
import com.gaea.single.bridge.dto.user.AnchorItemRes;
import com.gaea.single.bridge.dto.user.AnchorLabelRes;
import com.gaea.single.bridge.dto.user.AnchorProfileRes;
import com.gaea.single.bridge.enums.AnchorOnlineStatus;
import com.gaea.single.bridge.enums.GenderType;
import org.springframework.core.convert.converter.Converter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnchorConverter {
  public static final Converter<JSONObject, List<AnchorColumnRes>> toAnchorColumnResList =
      (result) -> {
        JSONArray array = result.getJSONArray("menuList");
        return array.stream()
            .sorted(Comparator.comparingInt(item -> ((JSONObject) item).getInteger("orderNumber")))
            .map(
                obj -> {
                  JSONObject item = ((JSONObject) obj);
                  return new AnchorColumnRes(item.getLong("id"), item.getString("name"));
                })
            .collect(Collectors.toList());
      };

  public static final Converter<JSONObject, AnchorItemRes> toAnchorItemRes =
      (result) -> {
        AnchorItemRes res = new AnchorItemRes();
        res.setUserId(result.getLong("userId"));
        res.setNickName(result.getString("nickName"));
        res.setGrade(result.getInteger("grade"));
        res.setGradeIcon(result.getString("gradeIcon"));
        res.setOnlineStatus(AnchorOnlineStatus.ofCode(result.getInteger("status")));
        res.setSignature(result.getString("comment"));
        res.setCover(result.getString("cover"));
        return res;
      };

  public static final Converter<JSONObject, AnchorProfileRes> toAnchorProfileRes =
      (result) -> {
        AnchorProfileRes res = new AnchorProfileRes();
        res.setUserId(result.getLong("id"));
        res.setShowId(result.getLong("showId"));
        res.setYunXinId(result.getString("yunxinId"));
        res.setOnlineStatus(AnchorOnlineStatus.ofCode(result.getInteger("status")));
        res.setNickName(result.getString("nickName"));
        res.setPortrait(result.getString("portrait"));
        res.setGradeIcon(result.getString("gradeIcon"));
        res.setPrice(result.getInteger("price"));
        res.setGender(GenderType.ofCode(result.getInteger("sex")));
        res.setAge(result.getInteger("age"));
        res.setAddress(result.getString("address"));
        res.setIntro(result.getString("intro"));
        Optional.ofNullable(result.getJSONArray("photos"))
            .map(
                photos -> photos.stream().map(photo -> (String) photo).collect(Collectors.toList()))
            .ifPresent(res::setPhotos);
        Optional.ofNullable(result.getJSONArray("labels"))
            .map(
                labels ->
                    labels.stream()
                        .map(
                            l -> {
                              JSONObject label = (JSONObject) l;
                              return new AnchorLabelRes(
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
