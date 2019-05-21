package com.fine.vegetables.utils;

/**
 * 计算两个经纬度的距离
 */

public class Distance {
    private static final double EARTH_RADIUS = 6378.137;
    //青岛经纬度
    public static final double QING_DAO_LAT = 36.07;
    public static final double QING_DAO_LNG = 120.39;
    //深圳经纬度
    public static final double SHEN_ZHEN_LAT = 22.55;
    public static final double SHEN_ZHEN_LNG = 114.06;

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double a = radLat1 - radLat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
