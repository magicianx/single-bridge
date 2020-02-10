package com.gaea.single.bridge.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
  private static final DateTimeFormatter LOBO_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-M-d");
  private static final DateTimeFormatter SINGLE_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy/M/d");

  public static String toLoboDate(String date) {
    return LOBO_DATE_FORMATTER.format(LocalDate.parse(date, SINGLE_DATE_FORMATTER));
  }

  public static String toSingleDate(String date) {
    return SINGLE_DATE_FORMATTER.format(LocalDate.parse(date, LOBO_DATE_FORMATTER));
  }
}