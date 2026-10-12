package com.guhecloud.cube.biz.dispatch.server.util;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

public class GeoToolUtil {
    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    /**
     * 创建一个点
     *
     * @param lng
     * @param lat
     *
     * @return
     */
    public static Point createPoint(double lng, double lat) {
        Coordinate coord = new Coordinate(lng, lat);
        return geometryFactory.createPoint(coord);
    }

    /**
     * 参数格式："POINT (1 1)"
     *
     * @param pointWKT
     *
     * @return
     *
     * @throws ParseException
     */
    public static Point createPointByWKT(String pointWKT) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        return (Point) reader.read(pointWKT);
    }

    /**
     * 通过坐标点 创建线
     *
     * @param coords
     *
     * @return
     */
    public static LineString createLine(List<Coordinate> coords) {
        return geometryFactory.createLineString(coords.toArray(new Coordinate[coords.size()]));
    }

    /**
     * 通过坐标点 创建多边形
     *
     * @param coords
     *
     * @return
     */
    public static Polygon createPolygon(List<Coordinate> coords) {
        return geometryFactory.createPolygon(coords.toArray(new Coordinate[coords.size()]));
    }

    /**
     * 创建一个圆，圆心(x,y) 半径RADIUS
     *
     * @param x
     * @param y
     * @param RADIUS 单位（米）
     *
     * @return
     */
    public static Polygon createCircle(double x, double y, final double RADIUS) {
        final int SIDES = 64;//圆上面的点个数
        //距离转度
        double degree = RADIUS / (2 * Math.PI * 6378137.0) * 360;

        Coordinate coords[] = new Coordinate[SIDES + 1];
        for (int i = 0; i < SIDES; i++) {
            double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
            double dx = Math.cos(angle) * degree;
            double dy = Math.sin(angle) * degree;
            coords[i] = new Coordinate((double) x + dx, (double) y + dy);
        }
        coords[SIDES] = coords[0];
        LinearRing ring = geometryFactory.createLinearRing(coords);
        Polygon polygon = geometryFactory.createPolygon(ring, null);
        return polygon;
    }
}

