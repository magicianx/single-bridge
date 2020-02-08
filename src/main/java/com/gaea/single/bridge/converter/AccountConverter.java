package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.GratuityGiftItemRes;
import com.gaea.single.bridge.dto.account.IncomeRes;
import com.gaea.single.bridge.enums.GiftType;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

public class AccountConverter {
  public static final Converter<Object, GratuityGiftItemRes> toGratuityGiftItemRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        GratuityGiftItemRes res = new GratuityGiftItemRes();
        res.setId(result.getLong("id"));
        res.setPrice(result.getLong("money"));
        res.setName(result.getString("pictureName"));
        res.setImgUrl(result.getString("pictureUrl"));
        res.setSmallImgUrl(result.getString("smallPictureUrl"));
        res.setType(GiftType.ofCode(result.getInteger("type")));
        res.setMinGrade(result.getInteger("minGrade"));
        res.setSvgUrl(result.getString("svgFileUrl"));
        return res;
      };

  public static final Converter<Object, IncomeRes> toIncomeRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        IncomeRes res = new IncomeRes();
        res.setIncomeAmount(result.getBigDecimal("money").divide(new BigDecimal("100")));
        res.setDiamonds(result.getLong("diamonds"));
        res.setAlipayAccount(result.getString("alipayNick"));
        res.setWithdrawable(result.getInteger("isCanWithdraw") == 1);
        res.setIsBindAlipay(result.getInteger("isBindAlipay") == 1);
        res.setIsFrozen(result.getInteger("isFrozen") == 1);
        res.setWithdrawLeastAmount(
            result.getBigDecimal("lowerLimit").divide(new BigDecimal("100")));
        res.setWithdrawDay(result.getInteger("withdrawalDay"));
        return res;
      };
}
