package com.gaea.single.bridge.util;

import java.math.BigDecimal;
import java.util.Optional;

public class LoboUtil {

  public static final BigDecimal MONEY_EXCHANGE_RATE = new BigDecimal("100");

  public static Boolean toBoolean(Integer value) {
    return value != null && value == 1;
  }

  public static BigDecimal toMoney(BigDecimal diamonds) {
    return Optional.ofNullable(diamonds)
        .map(d -> d.divide(MONEY_EXCHANGE_RATE))
        .orElse(BigDecimal.ZERO);
  }

  public static BigDecimal toDiamonds(BigDecimal money) {
    return Optional.ofNullable(money)
        .map(m -> m.multiply(MONEY_EXCHANGE_RATE))
        .orElse(BigDecimal.ZERO);
  }
}