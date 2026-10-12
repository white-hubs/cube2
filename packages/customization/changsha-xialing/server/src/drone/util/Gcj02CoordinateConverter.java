package com.guhecloud.cube.customization.xialing.server.drone.util;

import java.math.BigDecimal;

/**
 * GCJ-02与WGS84坐标转换工具。
 */
public final class Gcj02CoordinateConverter {
    private static final double PI = Math.PI;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    private Gcj02CoordinateConverter() {
    }

    /**
     * 将GCJ-02坐标转换为WGS84坐标；境外或未加密区域原样返回。
     */
    public static Coordinate toWgs84(BigDecimal longitude, BigDecimal latitude) {
        if (longitude == null || latitude == null) {
            return new Coordinate(longitude, latitude);
        }
        double lng = longitude.doubleValue();
        double lat = latitude.doubleValue();
        if (outOfChina(lng, lat)) {
            return new Coordinate(longitude, latitude);
        }
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLng = (dLng * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double gcjLat = lat + dLat;
        double gcjLng = lng + dLng;
        return new Coordinate(BigDecimal.valueOf(lng * 2 - gcjLng), BigDecimal.valueOf(lat * 2 - gcjLat));
    }

    private static boolean outOfChina(double longitude, double latitude) {
        return longitude < 72.004 || longitude > 137.8347 || latitude < 0.8293 || latitude > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320.0 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经纬度坐标。
     */
    public record Coordinate(BigDecimal longitude, BigDecimal latitude) {
    }
}
