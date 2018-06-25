package com.fcbox.spark.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 基于 JDK 8 time包的时间工具类
 *
 * @version 1.0.0
 * Date 2017年3月17日 上午9:40:32
 */
public final class Java8DateUtil {

    /**
     * 获取默认日期格式: yyyy-MM-dd
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateFormat.DATE_PATTERN_LINE.formatter;

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateFormat.DATETIME_PATTERN_LINE.formatter;

    private Java8DateUtil() {
        // no construct function
    }

    /**
     * String 转时间
     *
     * @param timeStr 日期时间（yyyy-MM-dd HH:mm:ss）
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * String 转时间
     *
     * @param timeStr 日期时间
     * @param format  时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(String timeStr, DateFormat format) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return LocalDateTime.parse(timeStr, format.formatter);
    }

    /**
     * 时间转 String
     *
     * @param time 时间日期
     * @return String （yyyy-MM-dd HH:mm:ss）
     */
    public static String parseTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    /**
     * 时间转 String
     *
     * @param time   时间日期
     * @param format 时间格式
     * @return String
     */
    public static String parseTime(LocalDateTime time, DateFormat format) {
        return format.formatter.format(time);
    }


    /**
     * String 日期转换
     *
     * @param dateStr 日期（yyyy-MM-dd）
     * @return yyyyMMdd
     */
    public static String lineDate2NoneLineDate(String dateStr) {
        return Java8DateUtil.parseNoneLineDate(Java8DateUtil.parseDate(dateStr));
    }

    /**
     * String 转日期
     *
     * @param dateStr 日期（yyyy-MM-dd）
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
    }

    /**
     * String 转日期
     *
     * @param dateStr 日期
     * @param format  时间格式
     * @return
     */
    public static LocalDate parseDate(String dateStr, DateFormat format) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, format.formatter);
    }

    /**
     * 时间转 String
     *
     * @param date 日期
     * @return String （yyyy-MM-dd）
     */
    public static String parseDate(LocalDate date) {
        return DEFAULT_DATE_FORMATTER.format(date);
    }


    /**
     * String 转日期
     *
     * @param dateStr 日期
     * @return
     */
    public static LocalDate parseNoneLineDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DateFormat.DATE_PATTERN_NONE.formatter);
    }

    /**
     * 时间转 String
     *
     * @param date 日期
     * @return String （yyyy-MM-dd）
     */
    public static String parseNoneLineDate(LocalDate date) {
        return parseDate(date, DateFormat.DATE_PATTERN_NONE);
    }

    /**
     * 时间转 String
     *
     * @param date   日期
     * @param format 时间格式
     * @return String
     */
    public static String parseDate(LocalDate date, DateFormat format) {
        return format.formatter.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return String （yyyyMMdd）
     */
    public static String getCurrentNoneLineDate() {
        return getCurrentDate(DateFormat.DATE_PATTERN_NONE);
    }

    /**
     * 获取当前时间
     *
     * @return String （yyyy-MM-dd）
     */
    public static String getCurrentDate() {
        return DEFAULT_DATE_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return String
     */
    public static String getCurrentDate(DateFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     *
     * @return String （yyyy-MM-dd HH:mm:ss）
     */
    public static String getCurrentDatetime() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return String
     */
    public static String getCurrentDatetime(DateFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * Date转LocalDate
     *
     * @param date 日期
     * @return LocalDate
     */
    public static LocalDate parseDate(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        } else {
            return parseTime(date).toLocalDate();
        }
    }

    /**
     * Date转LocalDateTime
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) date).toLocalDateTime();
        } else {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
    }

    /**
     * java.time.LocalDateTime --> java.util.Date
     *
     * @param localDateTime 时间
     * @return Date
     */
    public static Date localDateTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }


    /**
     * java.time.LocalDate --> java.util.Date
     *
     * @param localDate 日期
     * @return Date
     */
    public static Date localDateToUdate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 按天计算日期差
     *
     * @param startDate 开始日期 (yyyy-MM-dd)
     * @param endDate   结束日期 (yyyy-MM-dd)
     * @return long
     */
    public static long betweenDay(String startDate, String endDate) {
        return betweenDay(Java8DateUtil.parseDate(startDate), Java8DateUtil.parseDate(endDate));
    }

    /**
     * 按天计算日期差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long betweenDay(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(Objects.requireNonNull(startDate), Objects.requireNonNull(endDate));
    }

    /**
     * 获取日期段集合
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDates(String startDate, String endDate) {
        return getLocalDates(Java8DateUtil.parseDate(startDate), Java8DateUtil.parseDate(endDate));
    }

    /**
     * 获取日期段集合
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDates(LocalDate startDate, LocalDate endDate) {
        long betweenDay = Java8DateUtil.betweenDay(startDate, endDate);
        return LongStream.rangeClosed(0, betweenDay)
                .boxed()
                .map(value -> Objects.requireNonNull(startDate).plusDays(value))
                .collect(Collectors.toList());
    }

    /**
     * 获取日期段每月第一天和最后一天
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return List<Map>
     */
    public static Map<String, String> getMonthOfFirstAndLastDays(String startDate, String endDate) {
        return getMonthOfFirstAndLastDays(Java8DateUtil.parseDate(startDate), Java8DateUtil.parseDate(endDate));

    }

    /**
     * 获取日期段每月第一天和最后一天
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return List<Map>
     */
    public static Map<String, String> getMonthOfFirstAndLastDays(LocalDate startDate, LocalDate endDate) {
        long between = ChronoUnit.MONTHS.between(Objects.requireNonNull(startDate), Objects.requireNonNull(endDate));
        LocalDate beginDay = LocalDate.of(startDate.getYear(), startDate.getMonth(), 1);
        return LongStream.rangeClosed(0, between)
                .boxed()
                .map(value -> Objects.requireNonNull(beginDay).plusMonths(value))
                .collect(Collectors.toMap(Java8DateUtil::parseDate, localDate -> parseDate(localDate.with(TemporalAdjusters.lastDayOfMonth())), (u, v) -> u, LinkedHashMap::new));
    }

    /**
     * 时间格式
     */
    public enum DateFormat {

        /**
         * 短时间格式
         */
        DATE_PATTERN_LINE("yyyy-MM-dd"),
        DATE_PATTERN_SLASH("yyyy/MM/dd"),
        DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 长时间格式
         */
        DATETIME_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        DATETIME_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        DATETIME_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        DATETIME_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        DATETIME_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        DATETIME_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        DATETIME_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        DATETIME_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        private String pattern;

        DateFormat(String pattern) {
            this.pattern = pattern;
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        public String getPattern() {
            return pattern;
        }
    }

}
