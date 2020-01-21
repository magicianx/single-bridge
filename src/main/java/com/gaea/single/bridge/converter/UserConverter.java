package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.user.UserRes;
import org.springframework.core.convert.converter.Converter;

public class UserConverter {
  public static final Converter<Object, UserRes> toUserRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        UserRes res = new UserRes();
        res.setId(result.getLong("id"));
        res.setShowId(result.getString("showId"));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("portrait"));
        res.setIntro(result.getString("intro"));
        res.setGrade(result.getInteger("grade"));
        res.setGradeIconUrl(result.getString("gradeIcon"));
        res.setMobilePhone(result.getString("mobilePhone"));
        res.setIsPerfect(result.getInteger("isPerfect") == 2);
        res.setAmount(result.getLong("money"));
        return res;
      };
}
