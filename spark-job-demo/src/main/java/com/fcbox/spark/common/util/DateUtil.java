package com.fcbox.spark.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {


    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    public static final String DEFAULT_DAY = "yyyy-MM-dd";
    public static final String INT_DAY = "yyyyMMdd";


    public static Integer getIntDayByTime(Long time) {
        return Integer.parseInt(format(new Date(time), INT_DAY));
    }

    public static Long[] getTimeBetween(Date date) {
        Long[] res = new Long[2];
        long time = getZeroTime(date);
        res[0] = time;
        res[1] = time + 24 * 60 * 60 * 1000;
        return res;
    }

    public static Long[] getTimeBetween(Date startDate,Date endDate) {
        Long[] res = new Long[2];
        long time = getZeroTime(startDate);
        res[0] = time;
        endDate = addDays(endDate, 1);
        endDate = setZeroTime(endDate);
        res[1] = endDate.getTime();
        return res;
    }

    public static long getZeroTime(Date date) {
        Date date1 = setZeroTime(date);
        return date1.getTime();
    }

    public static Date setZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date time = calendar.getTime();
        return time;
    }


    /**
     * 获取零时零点的今天
     *
     * @return
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }


    /**
     * 获取一周的开始和结束日志
     *
     * @param date
     * @return
     */
    public static Date[] getWeekDay(Date date) {
        // 计算一周的开始日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Date startDate = getFirstDayOfWeek(date);
        // 结束日期
        calendar.add(Calendar.DATE, 1);
        // 周结束日期
        Date weekEndDate = getLastDayOfWeek(date);
        return new Date[]{startDate, weekEndDate};
    }

    /**
     * 获取一周的开始
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(2);
        c.setTime(date);
        c.set(7, c.getFirstDayOfWeek());
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(1, year);
        c.set(2, 0);
        c.set(5, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(5, week * 7);
        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取一周的结束日期
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(2);
        c.setTime(date);
        c.set(7, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return formatDate(new Date(), DEFAULT_PATTERN);
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay(Date date) {
        return formatDate(date, DEFAULT_PATTERN);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays(Date date) {
        return formatDate(date, "yyyyMMdd");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss.SSS格式
     *
     * @return
     */
    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     *
     * @return
     */
    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, DEFAULT_PATTERN);
        }
        return formatDate;
    }

    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, DEFAULT_PATTERN);
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (parseDate(s) == null || parseDate(e) == null) {
            return false;
        }
        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        return parse(date, DEFAULT_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 将时间设置到0点
     *
     * @param dateStr
     * @return
     */
    public static Date setToBeginOfDay(String dateStr) {
        Date date =parseDate(dateStr);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 将时间设置到23:59:59
     *
     * @param dateStr
     * @return
     */
    public static Date setToEndOfDay(String dateStr) {
        Date date =parseDate(dateStr);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 把日期转换为Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat(DEFAULT_PATTERN);
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
                    startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat(
                DEFAULT_PATTERN);
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    public static Date addDays(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, amount);
        return c.getTime();
    }

    public static Date add(Date date, int type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(type, amount);
        return cal.getTime();
    }

    public static List<Date> findDates(String begin, String end) {

        try {
            Date dBegin = parse(begin, DEFAULT_PATTERN);
            Date dEnd = parse(end, DEFAULT_PATTERN);
            List<Date> lDate = new ArrayList<Date>();
            lDate.add(dBegin);
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.setTime(dBegin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.setTime(dEnd);
            // 测试此日期是否在指定日期之后
            while (dEnd.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.DAY_OF_MONTH, 1);
                lDate.add(calBegin.getTime());
            }
            return lDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date now() {
        return new Date();
    }

    /**
     * 获取一段时间的环比改时间段上一段时间的开始时间
     * 如2017-12-05,2017-12-08,
     * 则上一段环比时间段是2017-12-01,2017-12-04
     * 返回2017-12-01
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Date getMoMDay(Date startDate, Date endDate) {
        int i = daysBetween(startDate, endDate);
        return DateUtils.addDays(startDate, -i - 1);
    }

    /**
     * 获取上一天
     *
     * @param day 格式:2017-12-01
     * @return
     */
    public static String getYestoday(String day) {
        Date date = parseDate(day);
        Date date1 = DateUtils.addDays(date, -1);
        return DateUtil.formatDate(date1);
    }

    public static String getYestoday() {
        Date date1 = DateUtils.addDays(new Date(), -1);
        return DateUtil.formatDate(date1);
    }

    /**
     * 获取上一周
     *
     * @param yearWeek 格式:2017/4
     * @return
     */
    public static String getLastWeek(String yearWeek) {

        String[] split = yearWeek.split("/");
        int year = Integer.parseInt(split[0]);
        int week = Integer.parseInt(split[1]);
        int lastweek = week - 1;
        if (lastweek == 0) {
            int lastyear = year - 1;
            lastweek = getMaxWeekNumOfYear(lastyear);
            year = lastyear;
        }
        return lastweek > 10 ? year + "/" + lastweek :  year + "/0" + lastweek ;
    }

    /**
     * 获取上一月
     *
     * @param yearMonth 格式:2017-04
     * @return
     */
    public static String getLastMonth(String yearMonth) {

        String[] split = yearMonth.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int lastmonth = month - 1;
        if (lastmonth == 0) {
            year = year - 1;
            lastmonth = 12;
        }
        return lastmonth > 10 ? year + "-" + lastmonth : year + "-0" + lastmonth;
    }

    /**
     * 获取当前时间所在年的周数
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 获取年的最大周数
     *
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    /**
     * 获取周一日期
     *
     * @param date
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int weekYear = cal.getFirstDayOfWeek();
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_PATTERN);
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        Date date = DateUtil.parseDate(smdate);
        Date date1 = DateUtil.parseDate(bdate);
        return daysBetween(date, date1);
    }
    /**
     * 得到某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);

        cal.set(Calendar.MONTH, month - 1);

        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));

        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 得到某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 将时间根据周期分段
     *
     * @param period
     *            1：日，2：周，3：月，4：季度，5：年
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Map<String, Object>> getPeriodList(int period, Date startDate, Date endDate) {
        Date lastDate = new Date();
        return getPeriodList(period, startDate, endDate, setToEndOfDay(getDay(addDays(lastDate, -1))));
    }

    /**
     * 将时间根据周期分段
     *
     * @param period
     *            1：日，2：周，3：月，4：季度，5：年
     * @param startDate
     * @param endDate
     * @param lastDate
     * @return
     */
    public static List<Map<String, Object>> getPeriodList(int period, Date startDate, Date endDate, Date lastDate) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        startDate = setToBeginOfDay(getDay(startDate));
        endDate = setToEndOfDay(getDay(endDate));
        lastDate = setToEndOfDay(getDay(lastDate));
        if (endDate.getTime() > lastDate.getTime()) {
            endDate = lastDate;
        }

        switch (period) {
            // 年
            case 5:
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                Calendar tempEndCal = Calendar.getInstance();
                tempEndCal.setTime(startDate);
                tempEndCal.add(Calendar.YEAR, 1);
                tempEndCal.add(Calendar.DAY_OF_YEAR, -1);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(endDate);

                while (tempEndCal.compareTo(endCal) <= 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", setToEndOfDay(getDay(tempEndCal.getTime())));

                    String title = startCal.get(Calendar.YEAR) + "年";
                    rangeMap.put("title", title);
                    resultList.add(rangeMap);
                    startCal.add(Calendar.YEAR, 1);
                    tempEndCal.setTime(startCal.getTime());
                    tempEndCal.add(Calendar.YEAR, 3);
                    tempEndCal.add(Calendar.DAY_OF_YEAR, -1);
                }
                // 剩余的日期
                if (startCal.compareTo(endCal) < 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", endDate);
                    rangeMap.put("title", format(startCal.getTime(), "DEFAULT_DAY.M.d")
                            + "-" + format(endDate, "DEFAULT_DAY.M.d"));
                    resultList.add(rangeMap);
                }
                break;
            // 季度
            case 4:
                startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                tempEndCal = Calendar.getInstance();
                tempEndCal.setTime(startDate);
                tempEndCal.add(Calendar.MONTH, 3);
                tempEndCal.add(Calendar.DAY_OF_YEAR, -1);

                endCal = Calendar.getInstance();
                endCal.setTime(endDate);

                while (tempEndCal.compareTo(endCal) <= 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", setToEndOfDay(getDay(tempEndCal.getTime())));

                    String title = startCal.get(Calendar.YEAR) + "年";
                    int month = startCal.get(Calendar.MONTH) + 1;
                    title += (month / 3 + 1) + "季度";
                    rangeMap.put("title", title);
                    resultList.add(rangeMap);
                    startCal.add(Calendar.MONTH, 3);
                    tempEndCal.setTime(startCal.getTime());
                    tempEndCal.add(Calendar.MONTH, 3);
                    tempEndCal.add(Calendar.DAY_OF_YEAR, -1);
                }
                // 剩余的日期
                if (startCal.compareTo(endCal) < 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", endDate);
                    rangeMap.put("title", format(startCal.getTime(), "DEFAULT_DAY.M.d")
                            + "-" + format(endDate, "DEFAULT_DAY.M.d"));
                    resultList.add(rangeMap);
                }
                break;
            // 月
            case 3:
                startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                tempEndCal = Calendar.getInstance();
                tempEndCal.setTime(startDate);
                tempEndCal.add(Calendar.MONTH, 1);
                tempEndCal.add(Calendar.DAY_OF_YEAR, -1);

                endCal = Calendar.getInstance();
                endCal.setTime(endDate);

                while (tempEndCal.compareTo(endCal) <= 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", setToEndOfDay(getDay(tempEndCal.getTime())));
                    rangeMap.put("title", startCal.get(Calendar.YEAR) + "." + (startCal.get(Calendar.MONTH) + 1));
                    resultList.add(rangeMap);
                    startCal.add(Calendar.MONTH, 1);
                    tempEndCal.setTime(startCal.getTime());
                    tempEndCal.add(Calendar.MONTH, 1);
                    tempEndCal.add(Calendar.DAY_OF_YEAR, -1);
                }
                // 剩余的日期
                if (startCal.compareTo(endCal) < 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", endDate);
                    rangeMap.put("title", format(startCal.getTime(), "DEFAULT_DAY.M.d")
                            + "-" + format(endDate, "DEFAULT_DAY.M.d"));
                    resultList.add(rangeMap);
                }
                break;
            // 周
            case 2:
                startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                tempEndCal = Calendar.getInstance();
                tempEndCal.setTime(startDate);
                tempEndCal.add(Calendar.DAY_OF_YEAR, 6);

                endCal = Calendar.getInstance();
                endCal.setTime(endDate);

                while (tempEndCal.compareTo(endCal) <= 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", setToEndOfDay(getDay(tempEndCal.getTime())));
                    rangeMap.put("title", format(startCal.getTime(), "DEFAULT_DAY.M.d") + "-"
                            + format(tempEndCal.getTime(), "DEFAULT_DAY.M.d"));
                    resultList.add(rangeMap);

                    tempEndCal.add(Calendar.DAY_OF_YEAR, 1);
                    startCal.setTime(tempEndCal.getTime());
                    tempEndCal.add(Calendar.DAY_OF_YEAR, 6);
                }
                // 剩余的日期
                if (startCal.compareTo(endCal) < 0) {
                    Map<String, Object> rangeMap = new HashMap<>();
                    rangeMap.put("startDate", setToBeginOfDay(getDay(startCal.getTime())));
                    rangeMap.put("endDate", endDate);
                    rangeMap.put("title", format(startCal.getTime(), "DEFAULT_DAY.M.d")
                            + "-" + format(endDate, "DEFAULT_DAY.M.d"));
                    resultList.add(rangeMap);
                }
                break;
            // 日
            case 1:
                Map<String, Object> rangeMap = new HashMap<>();
                rangeMap.put("startDate", startDate);
                rangeMap.put("endDate", endDate);
                rangeMap.put("title", format(startDate, "DEFAULT_DAY.M.d") + "-"
                        + format(endDate, "DEFAULT_DAY.M.d"));
                resultList.add(rangeMap);
                break;
            default:
                break;
        }

        return resultList;
    }


    public static void main(String[] args) {

        Long[] timeBetween = getTimeBetween(new Date(),parseDate("2018-03-21"));
        System.out.println(timeBetween[0]);
        System.out.println(timeBetween[1]);

    }


}
