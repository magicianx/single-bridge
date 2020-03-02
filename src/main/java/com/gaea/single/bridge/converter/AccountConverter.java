package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.LoboResult;
import com.gaea.single.bridge.dto.PageRes;
import com.gaea.single.bridge.dto.account.GratuityGiftItemRes;
import com.gaea.single.bridge.dto.account.IncomeRes;
import com.gaea.single.bridge.dto.account.OrderDetailRes;
import com.gaea.single.bridge.enums.GiftType;
import com.gaea.single.bridge.enums.OrderType;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.JsonUtils;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        res.setWithdrawable(LoboUtil.toBoolean(result.getInteger("isCanWithdraw")));
        res.setIsBindAlipay(LoboUtil.toBoolean(result.getInteger("isBindAlipay")));
        res.setIsFrozen(LoboUtil.toBoolean(result.getInteger("isFrozen")));
        res.setWithdrawLeastAmount(
            result.getBigDecimal("lowerLimit").divide(new BigDecimal("100")));
        res.setWithdrawDay(result.getInteger("withdrawalDay"));
        return res;
      };

  public static final Converter<Object, OrderDetailRes> toOrderDetailRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        OrderDetailRes res = new OrderDetailRes();
        res.setOrderNo(result.getString("businessId"));
        res.setType(OrderType.ofCode(result.getInteger("transactionType")));
        res.setTitle(result.getString("transactionTypeString"));
        res.setAmount(result.getString("moneyString"));
        res.setTime(DateUtil.toSingleDatetime(result.getLong("createTime")));
        res.setTradeUserShowId(result.getString("tradeUserShowId"));
        res.setTradeUserNickName(result.getString("tradeUserNickName"));
        res.setTradeUserPortraitUrl(result.getString("tradeUserPortrait"));
        return res;
      };
}
