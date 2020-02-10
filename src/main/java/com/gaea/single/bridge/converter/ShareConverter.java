package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.ShareInfoRes;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Optional;

public class ShareConverter {
  public static final Converter<Object, ShareInfoRes> toShareInfoRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ShareInfoRes res = new ShareInfoRes();
        BigDecimal withdrawableAmount = result.getBigDecimal("money");
        res.setWithdrawableAmount(withdrawableAmount);
        res.setRewardAmount(Optional.ofNullable(result.getBigDecimal("totalMoney")).orElse(BigDecimal.ZERO));
        res.setInviteCount(result.getInteger("shareNum"));

        boolean isBindAlipay = result.getInteger("isBindAlipay") == 1;
        BigDecimal leastAmount = result.getBigDecimal("leastMoney").divide(new BigDecimal(100));
        boolean withdrawable = withdrawableAmount.compareTo(leastAmount) > 0 && isBindAlipay;
        res.setWithdrawable(withdrawable);
        res.setRechargePercentage(result.getFloat("payPercentage"));
        res.setIncomePercentage(result.getFloat("incomePercentage"));
        res.setProfitDurationDays(result.getInteger("days"));
        return res;
      };
}
