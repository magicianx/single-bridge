package com.gaea.single.bridge.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
  private static final DateTimeFormatter LOBO_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-M-d");
  private static final DateTimeFormatter SINGLE_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy/M/d");
  private static final DateTimeFormatter SINGLE_DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
  private static final DateTimeFormatter DB_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final long DAY_MILLISECONDS = 24 * 60 * 60 * 1000L;

  public static String toLoboDate(String date) {
    return LOBO_DATE_FORMATTER.format(LocalDate.parse(date, SINGLE_DATE_FORMATTER));
  }

  public static LocalDate toLocalDate(String date) {
    return LocalDate.parse(date, SINGLE_DATE_FORMATTER);
  }

  public static String toSingleDate(String date) {
    return SINGLE_DATE_FORMATTER.format(LocalDate.parse(date, LOBO_DATE_FORMATTER));
  }

  public static String toSingleDatetime(Long timestamp) {
    LocalDateTime dateTime =
        Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    return SINGLE_DATETIME_FORMATTER.format(dateTime);
  }

  public static long getNowMilliseconds() {
    return getMilliseconds(LocalDateTime.now());
  }

  public static long getMilliseconds(LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
  }

  public static String toDbDate(LocalDate date) {
    return DB_DATE_FORMATTER.format(date);
  }

  /**
   * 毫秒数转换为天数, 向下取整
   *
   * @param milliseconds 毫秒数
   * @return 天数
   */
  public static int toFloorDays(long milliseconds) {
    if (milliseconds > 0) {
      return (int) Math.floor((double) milliseconds / DAY_MILLISECONDS);
    }
    return 0;
  }
}
