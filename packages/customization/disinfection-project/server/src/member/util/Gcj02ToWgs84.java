package com.guhecloud.cube.srv.gen.disinfection.server.member.util;

/**
 * 腾讯地图等国内图商使用 GCJ-02（火星坐标）；天地图逆地理等接口按 WGS84 经纬度解析（与 CGCS2000 在一般业务精度下可等同对待）。
 * <p>仅对调用第三方接口时的坐标做转换；若需与腾讯地图展示一致，业务库应保留原始 GCJ-02。
 */
public final class Gcj02ToWgs84 {

    private static final double PI = Math.PI;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    private Gcj02ToWgs84() {
    }

    /**
     * @param gcjLat GCJ-02 纬度
     * @param gcjLon GCJ-02 经度
     *
     * @return [0]=WGS84 纬度，[1]=WGS84 经度；境外或未加密区原样返回
     */
    public static double[] convert(double gcjLat, double gcjLon) {
        if (outOfChina(gcjLat, gcjLon)) {
            return new double[]{gcjLat, gcjLon};
        }
        double dLat = transformLat(gcjLon - 105.0, gcjLat - 35.0);
        double dLon = transformLon(gcjLon - 105.0, gcjLat - 35.0);
        double radLat = gcjLat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = gcjLat + dLat;
        double mgLon = gcjLon + dLon;
        return new double[]{gcjLat * 2 - mgLat, gcjLon * 2 - mgLon};
    }

    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
}
