package com.fine.vegetables.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static long formatTime(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
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
}
