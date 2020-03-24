package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.account.ShareInfoRes;
import com.gaea.single.bridge.dto.account.ShareInviteRecordRes;
import com.gaea.single.bridge.dto.account.ShareRewardRecordRes;
import com.gaea.single.bridge.enums.ShareRewardType;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Optional;

public class ShareConverter {
  public static final Converter<Object, ShareInfoRes> toShareInfoRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ShareInfoRes res = new ShareInfoRes();
        BigDecimal withdrawableAmount = LoboUtil.toMoney(result.getBigDecimal("money"));
        res.setWithdrawableAmount(withdrawableAmount);
        res.setRewardAmount(LoboUtil.toMoney(result.getBigDecimal("totalMoney")));
        res.setInviteCount(result.getInteger("shareNum"));

        boolean isBindAlipay = result.getInteger("isBindAlipay") == 1;
        BigDecimal leastAmount = LoboUtil.toMoney(result.getBigDecimal("leastMoney"));
        boolean withdrawable = withdrawableAmount.compareTo(leastAmount) > 0 && isBindAlipay;
        res.setWithdrawable(withdrawable);
        res.setRechargePercentage(result.getFloat("payPercentage"));
        res.setIncomePercentage(result.getFloat("incomePercentage"));
        res.setProfitDurationDays(result.getInteger("days"));
        return res;
      };

  public static final Converter<Object, ShareRewardRecordRes> toShareRewardRecordRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ShareRewardRecordRes res = new ShareRewardRecordRes();
        res.setUserId(result.getLong("userId"));
        res.setNickName(result.getString("nickName"));
        res.setType(ShareRewardType.ofCode(result.getInteger("type")));
        res.setMoney(LoboUtil.toMoney(result.getBigDecimal("money")));
        res.setCreateTime(result.getLong("createTime"));
        return res;
      };

  public static final Converter<Object, ShareInviteRecordRes> toShareInviteRecordRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        ShareInviteRecordRes res = new ShareInviteRecordRes();
        res.setUserId(result.getLong("userId"));
        res.setNickName(result.getString("nickName"));
        res.setPortraitUrl(result.getString("portrait"));
        res.setMoney(LoboUtil.toMoney(result.getBigDecimal("money")));
        return res;
      };
}
