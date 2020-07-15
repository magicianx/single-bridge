package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.FirstRechargeGiftConfigRes;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

/**
 * @author JunJYu
 * @date 2020/7/10 15:17
 */
public class PayConverter {

  public static final Converter<Object, FirstRechargeGiftConfigRes.RechargeGift> toRechargeGift =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        return new FirstRechargeGiftConfigRes.RechargeGift(
            result.getLong("id"),
            LoboUtil.toMoney(result.getBigDecimal("money")),
            result.getInteger("giftDiamonds"),
            result.getInteger("giftVipDays"));
      };
}
