package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.GratuityGiftItemRes;
import com.gaea.single.bridge.enums.GiftType;
import org.springframework.core.convert.converter.Converter;

public class AccountConverter {
  public static final Converter<JSONObject, GratuityGiftItemRes> toGratuityGiftItemRes =
      (result) -> {
        GratuityGiftItemRes res = new GratuityGiftItemRes();
        res.setId(result.getLong("id"));
        res.setAmount(result.getLong("money"));
        res.setName(result.getString("pictureName"));
        res.setImg(result.getString("pictureUrl"));
        res.setSmallImg(result.getString("smallPictureUrl"));
        res.setType(GiftType.ofCode(result.getInteger("type")));
        res.setMinGrade(result.getInteger("minGrade"));
        res.setSvg(result.getString("svgFileUrl"));
        return res;
      };
}
