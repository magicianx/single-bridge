package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.*;
import com.gaea.single.bridge.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserConverter {
  public static final Converter<Object, List<UserColumnRes>> toUserColumnResList =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        JSONArray array = result.getJSONArray("menuList");
        return array.stream()
            .sorted(Comparator.comparingInt(item -> ((JSONObject) item).getInteger("orderNumber")))
            .map(
                i -> {
                  JSONObject item = ((JSONObject) i);
                  return new UserColumnRes(item.getLong("id"), item.getString("name"));
                })
            .collect(Collectors.toList());
      };

  public static final Converter<Object, UserItemRes> toUserItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserType userType = UserType.ofCode(result.getInteger("userType"));

        UserItemRes res = new UserItemRes();
        res.setUserId(result.getLong("userId"));
        res.setYunXinId(result.getString("yunxinId"));
        res.setUserType(userType);
        res.setNickName(result.getString("nickName"));
        res.setGrade(result.getInteger("grade"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        res.setSignature(result.getString("comment"));
        res.setCoverUrl(result.getString("cover"));
        res.setPrice(userType == UserType.ANCHOR ? result.getInteger("price") : null);
        return res;
      };

  public static final Converter<Object, UserProfileRes> toUserProfileRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserType userType = UserType.ofCode(result.getInteger("userType"));

        UserProfileRes res = new UserProfileRes();
        res.setUserId(result.getLong("id"));
        res.setUserType(userType);
        res.setShowId(result.getString("showId"));
        res.setYunXinId(result.getString("yunxinId"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("portrait"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setPrice(userType == UserType.ANCHOR ? result.getInteger("price") : null);
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

  public static final Converter<Object, UserInfoRes> toUserRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserInfoRes res = new UserInfoRes();
        res.setId(result.getLong("userId"));
        res.setShowId(result.getString("showId"));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("portrait"));
        res.setIntro(result.getString("intro"));
        res.setGrade(result.getInteger("grade"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setMobilePhone(result.getString("mobilePhone"));
        res.setBalance(result.getLong("money"));
        res.setIsPerfectBirthday(result.getInteger("isPerfectAge") == 1);
        res.setIsPerfectGender(result.getInteger("isPerfectSex") == 1);
        res.setUserType(UserType.ofCode(result.getInteger("userType")));
        res.setInviteCode(result.getString("inviteCode"));
        res.setAuthStatus(AnchorAuthStatus.ofCode(result.getInteger("isVideoAudit")));
        return res;
      };

  public static final Converter<Object, LoginRes> toLoginRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        LoginRes res = new LoginRes();
        res.setId(result.getLong("userId"));
        res.setSession(result.getString("session"));
        res.setYunXinId(result.getString("yunxinAccid"));
        res.setGender(GenderType.ofCode(result.getInteger("sex")));
        res.setFullPhoneNum(result.getString("mobilePhone"));
        if (StringUtils.isNotBlank(result.getString("birthday"))) {
          res.setBirthday(DateUtil.toSingleDate(result.getString("birthday")));
        }
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        return res;
      };

  public static final Converter<Object, AlbumItemRes> toAlbumItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        AlbumItemRes res = new AlbumItemRes();
        res.setImgUrl(result.getString("url"));
        res.setStatus(AuditStatus.ofCode(result.getInteger("status")));
        res.setIsCover(false);
        return res;
      };
}
