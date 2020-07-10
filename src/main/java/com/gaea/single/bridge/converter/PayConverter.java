package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.RechargeGiftRes;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

/**
 * @author JunJYu
 * @date 2020/7/10 15:17
 */
public class PayConverter {

  public static final Converter<Object, RechargeGiftRes.RechargeGift> toRechargeGiftRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
          return new RechargeGiftRes.RechargeGift(
            result.getLong("id"),
            LoboUtil.toMoney(result.getBigDecimal("money")),
            result.getInteger("giftDiamonds"),
            result.getInteger("giftVipDays"));
      };
}
