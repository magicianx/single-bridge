package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.user.AlbumItemRes;
import com.gaea.single.bridge.dto.user.LoginRes;
import com.gaea.single.bridge.dto.user.UserProfileRes;
import com.gaea.single.bridge.enums.AuditStatus;
import com.gaea.single.bridge.enums.GenderType;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class UserConverter {
  public static final Converter<Object, UserProfileRes> toUserRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserProfileRes res = new UserProfileRes();
        res.setId(result.getLong("userId"));
        res.setShowId(result.getLong("showId"));
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
