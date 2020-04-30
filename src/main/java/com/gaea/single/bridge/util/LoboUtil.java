package com.gaea.single.bridge.util;

import com.gaea.single.bridge.config.DictionaryProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class LoboUtil {

  public static final BigDecimal MONEY_EXCHANGE_RATE = new BigDecimal("100");

  /**
   * 1为true , 其他值为false
   *
   * @param value lobo端值
   * @return boolean
   */
  public static boolean toBoolean(Integer value) {
    return value != null && value == 1;
  }

  /**
   * 2为true , 其他值为false
   *
   * @param value lobo端值
   * @return boolean
   */
  public static boolean toBoolean2(Integer value) {
    return value != null && value == 2;
  }

  public static BigDecimal toMoney(BigDecimal diamonds) {
    return Optional.ofNullable(diamonds)
        .map(d -> d.divide(MONEY_EXCHANGE_RATE, 2, RoundingMode.FLOOR))
        .orElse(BigDecimal.ZERO);
  }

  public static int toDiamonds(BigDecimal money) {
    return Optional.ofNullable(money)
        .map(m -> m.multiply(MONEY_EXCHANGE_RATE).intValue())
        .orElse(0);
  }

  public static String getImageUrl(String imgPath){
    return DictionaryProperties.get().getLobo().getImgHost() + imgPath;
  }
}
