package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.config.DictionaryProperties;
import com.gaea.single.bridge.dto.account.*;
import com.gaea.single.bridge.enums.GiftType;
import com.gaea.single.bridge.enums.OrderType;
import com.gaea.single.bridge.enums.UserOnlineStatus;
import com.gaea.single.bridge.enums.UserType;
import com.gaea.single.bridge.util.DateUtil;
import com.gaea.single.bridge.util.LoboUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

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
        res.setIncomeAmount(LoboUtil.toMoney(result.getBigDecimal("money")));
        res.setDiamonds(result.getLong("diamonds"));
        res.setAlipayAccount(result.getString("alipayNick"));
        res.setWithdrawable(LoboUtil.toBoolean(result.getInteger("isCanWithdraw")));
        res.setIsBindAlipay(LoboUtil.toBoolean(result.getInteger("isBindAlipay")));
        res.setIsFrozen(LoboUtil.toBoolean(result.getInteger("isFrozen")));
        res.setWithdrawLeastAmount(LoboUtil.toMoney(result.getBigDecimal("lowerLimit")));
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
        res.setVideoDurationSecond(result.getInteger("duration"));
        res.setTime(DateUtil.toSingleDatetime(result.getLong("createTime")));
        res.setTradeUserId(result.getLong("tradeUserId"));
        res.setTradeUserShowId(result.getString("tradeUserShowId"));
        res.setTradeUserNickName(result.getString("tradeUserNickName"));
        res.setTradeUserPortraitUrl(result.getString("tradeUserPortrait"));
        return res;
      };

  public static final Converter<Object, WechatPayRes> toWechatPayRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        WechatPayRes res = new WechatPayRes();
        res.setAppId(result.getString("appid"));
        res.setPrepayId(result.getString("prepayid"));
        res.setSign(result.getString("sign"));
        res.setTimestamp(result.getString("timestamp"));
        res.setMchId(result.getString("partnerid"));
        res.setNonceStr(result.getString("noncestr"));
        return res;
      };

  public static final Converter<Object, RankMenuRes> toRankGroupRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;

        List<RankMenuRes.Menu> menus =
            result.getJSONArray("child").stream()
                .map(
                    i -> {
                      JSONObject item = (JSONObject) i;
                      return new RankMenuRes.Menu(item.getLong("menuId"), item.getString("name"));
                    })
                .collect(Collectors.toList());

        return new RankMenuRes(null, result.getString("name"), menus);
      };

  public static final Converter<Object, RankingRes> toRankingRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        JSONArray users = result.getJSONArray("users");
        Long differDiamonds = result.getLong("differDiamonds");
        UserType userType = UserType.ofCode(result.getInteger("userType"));

        String tipText = null;
        if (differDiamonds != null && userType != null) {
          DictionaryProperties.Ranking rankingConfig = DictionaryProperties.get().getRanking();
          String template =
              UserType.ANCHOR.equals(userType)
                  ? rankingConfig.getAnchorTipText()
                  : rankingConfig.getUserTipText();
          tipText = String.format(template, differDiamonds);
        }

        List<RankingRes.RankUser> rankUsers =
            users.stream()
                .map(
                    u -> {
                      JSONObject user = (JSONObject) u;
                      RankingRes.RankUser rankUser = new RankingRes.RankUser();
                      rankUser.setUserId(user.getLong("userId"));
                      rankUser.setNickName(user.getString("nickName"));
                      rankUser.setPortraitUrl(user.getString("portrait"));
                      rankUser.setRanking(user.getInteger("ranking"));
                      rankUser.setOnlineStatus(UserOnlineStatus.ofCode(user.getInteger("status")));
                      rankUser.setGradeIconUrl(user.getString("gradeIcon"));
                      rankUser.setIsVip(LoboUtil.toBoolean(user.getInteger("isVip")));
                      return rankUser;
                    })
                .collect(Collectors.toList());
        return new RankingRes(tipText, rankUsers);
      };
}
