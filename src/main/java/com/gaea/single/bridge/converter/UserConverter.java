package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.constant.DefaultSettingConstant;
import com.gaea.single.bridge.dto.user.*;
import com.gaea.single.bridge.enums.*;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.LoboUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            .filter(
                i -> {
                  JSONObject item = ((JSONObject) i);
                  return !item.getLong("id")
                      .equals(DictionaryProperties.get().getAppStoreAudit().getUserColumnId());
                })
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
        res.setIsUp(LoboUtil.toBoolean(result.getInteger("isUp")));
        res.setPrice(userType == UserType.ANCHOR ? result.getInteger("price") : null);
        // 只有用户为普通用户时才返回对应的vip状态
        res.setIsVip(
            userType == UserType.GENERAL_USER
                && LoboUtil.toBoolean(result.getInteger("isSuperVip")));
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
                : DefaultSettingConstant.UNKNOWN_POSITION);
        res.setIntro(result.getString("intro"));
        res.setFansNum(result.getInteger("fansNum"));
        res.setFollowNum(result.getInteger("followNum"));
        res.setIsVip(LoboUtil.toBoolean(result.getInteger("isSuperVip")));
        res.setFollowStatus(
            Optional.ofNullable(result.getInteger("followStatus"))
                .map(FollowStatus::ofCode)
                .orElse(FollowStatus.UNFOLLOW));

        List<String> photos = new ArrayList<>();
        if (result.getJSONArray("photos") != null) {
          result.getJSONArray("photos").forEach(photo -> photos.add((String) photo));
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
        res.setFansNum(result.getInteger("fansNum"));
        res.setFollowNum(result.getInteger("followNum"));
        Integer isVideoAudit = result.getInteger("isVideoAudit");
        res.setAuthStatus(
            isVideoAudit != null ? AnchorAuthStatus.ofCode(isVideoAudit) : AnchorAuthStatus.UNAUTH);
        res.setIsVip(LoboUtil.toBoolean(result.getInteger("isSuperVip")));
        res.setIsBindPhone(LoboUtil.toBoolean(result.getInteger("isBindPhone")));
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
        res.setIsRegister(LoboUtil.toBoolean(result.getInteger("isRegist")));
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

  public static final Converter<Object, UserGradeRes> toUserGradeRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserGradeRes res = new UserGradeRes();
        res.setCurrentXp(result.getLong("currentLevel"));
        result
            .getJSONArray("grades")
            .forEach(grade -> res.getGradeXps().add(Long.valueOf(grade.toString())));
        res.setSetTopNum(result.getInteger("setTopNum"));
        res.setSetTopDurationTime(result.getInteger("setTopTime"));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("status")));
        return res;
      };

  public static final Converter<Object, FollowItemRes> toFollowItemRes =
      (obj) -> {
        FollowItemRes res = new FollowItemRes();
        fillFollowItemRes(obj, res);
        return res;
      };

  public static final Converter<Object, FansItemRes> toFansItemRes =
      (obj) -> {
        FansItemRes res = new FansItemRes();
        fillFollowItemRes(obj, res);
        return res;
      };

  public static final Converter<Object, UserVideoItemRes> toUserVideoItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserVideoItemRes res = new UserVideoItemRes();
        res.setId(result.getLong("id"));
        res.setCloudFileId(result.getString("unid"));
        res.setCoverUrl(result.getString("coverUrl"));
        res.setVideoUrl(result.getString("videoUrl"));
        res.setAuditStatus(AuditStatus.ofCode(result.getInteger("status")));
        res.setIsPraise(LoboUtil.toBoolean(result.getInteger("isPriase")));
        res.setPraiseTimes(result.getLong("praiseTimes"));
        res.setGratuityMoney(result.getLong("rewardTotalMoney"));
        return res;
      };

  public static final Converter<Object, VipConfigItemRes> toVipConfigItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        VipConfigItemRes res = new VipConfigItemRes();
        res.setId(result.getLong("id"));
        res.setType(VipType.ofCode(result.getInteger("type")));
        BigDecimal diamonds = result.getBigDecimal("vipPrice");
        res.setDiamonds(diamonds.intValue());
        res.setPrice(LoboUtil.toMoney(diamonds));
        res.setDayPrice(
            diamonds.divide(
                result.getBigDecimal("duration").multiply(LoboUtil.MONEY_EXCHANGE_RATE),
                2,
                RoundingMode.FLOOR));
        return res;
      };

  public static final Converter<Object, ViewRecordItemRes> toViewRecordItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ViewRecordItemRes res = new ViewRecordItemRes();
        res.setUserId(result.getLong("overUserId"));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("overUserIdHeadUrl"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setYunXinId(result.getString("yunxinId"));
        res.setViewTimestamp(result.getLong("createTime"));
        res.setIsVip(LoboUtil.toBoolean(result.getInteger("isSupperVip")));
        res.setOnlineStatus(UserOnlineStatus.ofCode(result.getInteger("userStatus")));
        return res;
      };

  private static <T extends FollowItemRes> void fillFollowItemRes(Object obj, T res) {
    JSONObject result = (JSONObject) obj;
    res.setUserId(result.getLong("userId"));
    res.setNickName(result.getString("nickName"));
    res.setPortraitUrl(result.getString("portrait"));
    res.setGrade(result.getInteger("grade"));
    res.setGradeHeadUrl(result.getString("gradeHeadUrl"));
    res.setIntro(result.getString("intro"));
    res.setFollowStatus(FollowStatus.ofCode(result.getInteger("followStatus")));
    UserType userType = UserType.ofCode(result.getInteger("userType"));
    // 只有用户为普通用户时才返回对应的vip状态
    res.setIsVip(
        userType == UserType.GENERAL_USER && LoboUtil.toBoolean(result.getInteger("isSuperVip")));
  }
}
