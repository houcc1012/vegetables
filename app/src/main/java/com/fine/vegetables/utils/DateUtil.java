package com.fine.vegetables.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_NOT_HOUR = "MM月dd日";
    public static final String FORMAT_DATE_ARENA = "yyy-MM-dd";
    public static final String DEFAULT_FORMAT_NOT_SECOND = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_SPECIAL_SLASH = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_HOUR = "HH:mm";


    public static final String TODAY = "今天";
    public static final String TOMORROW = "明天";

    public static long format(String strTime, String fromFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();
            return currentTime;
        }
    }

    public static String format(long time, String toFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(toFormat);
        return dateFormat.format(new Date(time));
    }

    public static String format(Date date, String toFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(toFormat);
        return dateFormat.format(date);
    }


    public static String format(String time, String fromFormat, String toFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
        try {
            Date date = dateFormat.parse(time);
            dateFormat = new SimpleDateFormat(toFormat);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String addDate(Date todayDate, int date, String toFormat) {
        Calendar toadyCalendar = Calendar.getInstance();
        toadyCalendar.setTime(todayDate);
        toadyCalendar.add(Calendar.DAY_OF_YEAR, date);
        return format(toadyCalendar.getTime(), toFormat);
    }

    public static String addMinute(Date todayDate, int minute, String toFormat) {
        Calendar toadyCalendar = Calendar.getInstance();
        toadyCalendar.setTime(todayDate);
        toadyCalendar.add(Calendar.MINUTE, minute);
        return format(toadyCalendar.getTime(), toFormat);
    }

    public static String addHour(Date todayDate, int hour, String toFormat) {
        Calendar toadyCalendar = Calendar.getInstance();
        toadyCalendar.setTime(todayDate);
        toadyCalendar.add(Calendar.HOUR_OF_DAY, hour);
        return format(toadyCalendar.getTime(), toFormat);
    }

    /**
     * 获取当前时间下一小时的整点小时时间
     *
     * @param date
     * @return
     */
    public static Date getNextHourTime(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.add(Calendar.HOUR_OF_DAY, 1);
        date = ca.getTime();
        return date;
    }


    public static boolean isTomorrow(long time, long today) {
        Date date = new Date(time);
        Date todayDate = new Date(today);
        return isTomorrow(date, todayDate);
    }

    public static boolean isTomorrow(Date date, Date todayDate) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(todayDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        todayCalendar.add(Calendar.DAY_OF_YEAR, 1);
        return isToday(calendar.getTime(), todayCalendar.getTime());
    }

    public static boolean isToday(Date date, Date todayDate) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(todayDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && todayCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }


    public static String getDayOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String result = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                result = "周一";
                break;
            case Calendar.TUESDAY:
                result = "周二";
                break;
            case Calendar.WEDNESDAY:
                result = "周三";
                break;
            case Calendar.THURSDAY:
                result = "周四";
                break;
            case Calendar.FRIDAY:
                result = "周五";
                break;
            case Calendar.SATURDAY:
                result = "周六";
                break;
            case Calendar.SUNDAY:
                result = "周日";
                break;
            default:
                break;
        }
        return result;
    }


}
