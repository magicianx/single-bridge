package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.GratuityGiftItemRes;
import com.gaea.single.bridge.enums.GiftType;
import org.springframework.core.convert.converter.Converter;

public class AccountConverter {
  public static final Converter<Object, GratuityGiftItemRes> toGratuityGiftItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        GratuityGiftItemRes res = new GratuityGiftItemRes();
        res.setId(result.getLong("id"));
        res.setAmount(result.getLong("money"));
        res.setName(result.getString("pictureName"));
        res.setImgUrl(result.getString("pictureUrl"));
        res.setSmallImgUrl(result.getString("smallPictureUrl"));
        res.setType(GiftType.ofCode(result.getInteger("type")));
        res.setMinGrade(result.getInteger("minGrade"));
        res.setSvgUrl(result.getString("svgFileUrl"));
        return res;
      };
}
