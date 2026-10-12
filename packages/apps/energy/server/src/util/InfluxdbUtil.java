package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteDeviceVo;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

@Component
public class InfluxdbUtil {
    private static String dbName;

    @Value("${influx.dbNameEnergy}")
    public void setDbName(String dbName) {
        InfluxdbUtil.dbName = dbName;
    }

    private static String dborg;

    @Value("${influx.dborg}")
    public void setDborg(String dborg) {
        InfluxdbUtil.dborg = dborg;
    }

    private static InfluxDBClient influxdbClient;

    @Autowired
    public void setInfluxdbClient(InfluxDBClient influxdbClient) {
        InfluxdbUtil.influxdbClient = influxdbClient;
    }

    public static void inserPoints(List<Point> points) {
        WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
        writeApiBlocking.writePoints(dbName, dborg, points);
    }

    public static DataVo getDeviceLatestDataInDB(LocalDateTime dateTime, DateEnum dateEnum, String deviceId) {
        String influxQuerySql = "";
        if (dateEnum.toString().equals("Hour")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
                             "  |> filter(fn: (r) => r[\"deviceId\"] ==\"" + deviceId + "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Day")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceDay\")\n" +
                             "  |> filter(fn: (r) => r[\"deviceId\"] ==\"" + deviceId + "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Month")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -1);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.MONTHS, 1);
            String startTime = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String endTime = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceMonth\")\n" +
                             "  |> filter(fn: (r) => r[\"deviceId\"] ==\"" + deviceId + "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Year")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.YEARS, -1);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.YEARS, 1);
            String startTime = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String endTime = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceYear\")\n" +
                             "  |> filter(fn: (r) => r[\"deviceId\"] ==\"" + deviceId + "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";
        }

        return execQueryForDataVo(influxQuerySql, influxdbClient);
    }

    /**
     * 得到分项最新的一条数据
     *
     * @param dateTime
     * @param dateEnum
     *
     * @return
     */
    public static DataVo getItemLatestDataInDB(LocalDateTime dateTime, DateEnum dateEnum, Long deviceId, Long projectId) {
        String influxQuerySql = "";
        if (dateEnum.toString().equals("Hour")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"projectHour\" )\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.projectId == \"" + projectId +
                             "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Day")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"projectDay\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.projectId == \"" + projectId +
                             "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Month")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -6);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.MONTHS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"projectMonth\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.projectId == \"" + projectId +
                             "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Year")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.YEARS, -1);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.YEARS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"projectYear\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.projectId == \"" + projectId +
                             "\" and r[\"instrumentType\"] !=\"\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";
        }

        return execQueryForDataVo(influxQuerySql, influxdbClient);
    }

    /**
     * 得到分项最新的一条数据
     *
     * @param dateTime
     * @param dateEnum
     *
     * @return
     */
    public static DataVo getAreaLatestDataInDB(LocalDateTime dateTime, DateEnum dateEnum, Long deviceId, Long areaId) {
        String influxQuerySql = "";
        if (dateEnum.toString().equals("Hour")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaHour\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.areaId == \"" + areaId + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Day")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.areaId == \"" + areaId + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Month")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -6);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.MONTHS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaMonth\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.areaId == \"" + areaId + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Year")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.YEARS, -1);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.YEARS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaYear\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\" and r.areaId == \"" + areaId + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";
        }

        return execQueryForDataVo(influxQuerySql, influxdbClient);
    }

    public static DataVo getRouteLatestDataInDB(InfluxDBClient influxdbClient, LocalDateTime dateTime, DateEnum dateEnum,
                                                RouteDeviceVo routeDeviceVo
                                               ) {
        String influxQuerySql = "";
        if (dateEnum.toString().equals("Hour")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"routeHour\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + routeDeviceVo.getDeviceId() + "\" and r.routeId == \"" +
                             routeDeviceVo.getRouteId() + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Day")) {
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: -30d)\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"routeDay\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + routeDeviceVo.getDeviceId() + "\" and r.routeId == \"" +
                             routeDeviceVo.getRouteId() + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Month")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -6);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.MONTHS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"routeMonth\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + routeDeviceVo.getDeviceId() + "\" and r.routeId == \"" +
                             routeDeviceVo.getRouteId() + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";

        } else if (dateEnum.toString().equals("Year")) {
            LocalDateTime lastMonth = DateUtils.offset(dateTime, ChronoUnit.YEARS, -1);
            LocalDateTime offset = DateUtils.offset(dateTime, ChronoUnit.YEARS, 1);
            String s = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            String s1 = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            influxQuerySql = "from(bucket: \"energy\")\n" +
                             "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                             "  |> filter(fn: (r) => r[\"_measurement\"] == \"routeHour\")\n" +
                             "  |> filter(fn: (r) => r.deviceId ==\"" + routeDeviceVo.getDeviceId() + "\" and r.routeId == \"" +
                             routeDeviceVo.getRouteId() + "\")\n" +
                             "  |> last()\n" +
                             "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";
        }

        return execQueryForDataVo(influxQuerySql, influxdbClient);
    }

    public static String getLatestReadMeterLogTableQuery(String deviceId) {
        String influxdbQuery = "from(bucket: \"energy\")\n" +
                               "  |> range(start: -30d)\n" +
                               "  |> filter(fn: (r) => r[\"_measurement\"] == \"readMeterLog\")\n" +
                               "  |> filter(fn: (r) => r.deviceId ==\"" + deviceId + "\")\n" +
                               "  |> last()\n" +
                               "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n";
        return influxdbQuery;
    }

    public static DataVo execQueryForDataVo(String influxQuerySql, InfluxDBClient influxdbClient) {
        if (StringUtils.isNotBlank(influxQuerySql)) {
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询出数据库中最新的一条数据，也就是上次读数
            List<DataVo> dataList = queryApi.query(influxQuerySql, DataVo.class);
            dataList.sort(Comparator.comparing(DataVo::getTime).reversed());
            return CollectionUtils.isNotEmpty(dataList) ? dataList.getLast() : null;
        }
        return null;
    }

    public static <T> T execQueryForT(String influxQuerySql, InfluxDBClient influxdbClient, Class<T> clazz) {
        if (StringUtils.isNotBlank(influxQuerySql)) {
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询出数据库中最新的一条数据，也就是上次读数
            List<T> dataList = queryApi.query(influxQuerySql, clazz);
            return CollectionUtils.isNotEmpty(dataList) ? dataList.getLast() : null;
        }
        return null;
    }

    public static <T> T execQueryForT(String influxQuerySql, Class<T> clazz) {
        if (StringUtils.isNotBlank(influxQuerySql)) {
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询出数据库中最新的一条数据，也就是上次读数
            List<T> dataList = queryApi.query(influxQuerySql, clazz);
            return CollectionUtils.isNotEmpty(dataList) ? dataList.getLast() : null;
        }
        return null;
    }

    public static <T> List<T> execQueryForList(String influxQuerySql, InfluxDBClient influxdbClient, Class<T> clazz) {
        if (StringUtils.isNotBlank(influxQuerySql)) {
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询出数据库中最新的一条数据，也就是上次读数
            List<T> dataList = queryApi.query(influxQuerySql, clazz);
            return dataList;
        }
        return null;
    }

    public static <T> List<T> execQueryForList(String influxQuerySql, Class<T> clazz) {
        if (StringUtils.isNotBlank(influxQuerySql)) {
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询出数据库中最新的一条数据，也就是上次读数
            List<T> dataList = queryApi.query(influxQuerySql, clazz);
            return dataList;
        }
        return null;
    }

    //写数据，如果time时间相同的话即可覆盖
    public static void writePoint(Point point) {
        WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
        writeApiBlocking.writePoint(dbName, dborg, point);
    }

    //将时间转换成标准格式
    public static String getFormatTimeByInstant(Instant instant, DateEnum dateEnum) {
        LocalDateTime localDateTime = DateUtils.parse(instant.toEpochMilli());
        String formatTime = localDateTime.format(DateTimeFormatter.ofPattern(switchTimeFormat(dateEnum)));
        return formatTime;
    }

    /**
     * 返回时间格式
     *
     * @param dateEnum
     *
     * @return
     */
    public static String switchTimeFormat(DateEnum dateEnum) {
        String timeFormat;
        switch (dateEnum) {
            case Hour:
                timeFormat = "yyyy-MM-dd HH:00:00";
                break;
            case Day:
                timeFormat = "yyyy-MM-dd 00:00:00";
                break;
            case Month:
                timeFormat = "yyyy-MM-01 00:00:00";
                break;
            case Year:
                timeFormat = "yyyy-01-01 00:00:00";
                break;
            default:
                timeFormat = "";

        }
        return timeFormat;
    }

    public static String convertTimeToFormatStr(LocalDateTime dateTime, DateEnum dateEnum) {
        String date;
        switch (dateEnum) {
            case Hour:
                date = DateUtils.format(dateTime, "yyyy-MM-dd HH:00:00");
                break;
            case Day:
                date = DateUtils.format(dateTime, "yyyy-MM-dd 00:00:00");
                break;
            case Month:
                date = DateUtils.format(dateTime, "yyyy-MM-01 00:00:00");
                break;
            case Year:
                date = DateUtils.format(dateTime, "yyyy-01-01 00:00:00");
                break;
            default:
                date = "";
        }
        return date;
    }

    /**
     * 获取数据聚合的时间，就是influxdb的time字段
     *
     * @param dateEnum
     * @param dateTime
     *
     * @return
     */
    public static LocalDateTime getAggTime(DateEnum dateEnum, LocalDateTime dateTime) {
        LocalDateTime formatDateTime;
        switch (dateEnum) {
            case Hour:
                formatDateTime = DateUtils.parse(DateUtils.format(dateTime, "yyyy-MM-dd HH:00:00"));

                break;
            case Day:
                formatDateTime = DateUtils.parse(DateUtils.format(dateTime, "yyyy-MM-dd 00:00:00"));
                break;
            case Month:
                formatDateTime = DateUtils.parse(DateUtils.format(dateTime, "yyyy-MM-01 00:00:00"));
                break;
            case Year:
                formatDateTime = DateUtils.parse(DateUtils.format(dateTime, "yyyy-01-01 00:00:00"));
                break;
            default:
                formatDateTime = dateTime;

        }
        return formatDateTime;
    }

    public static boolean isNumeric(String str) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            Integer i = bigDecimal.intValue();
            Integer a = i.MAX_VALUE;
            if (i.intValue() == a.intValue() || i.intValue() < 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static String getInfluxdbDeviceIdQuery(String deviceIds) {
        String condition = "";
        if (StringUtils.isNotBlank(deviceIds)) {
            condition = Arrays.stream(deviceIds.split(","))
                    .map(deviceId -> "r.deviceId == \"" + deviceId + "\"")
                    .collect(Collectors.joining(" or "));
            condition = " and (" + condition + ")";
        }
        return condition;
    }

    public static String getInfluxdbDeviceIdQuery(List<Long> deviceIds) {
        String condition = "";
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            condition = deviceIds.stream()
                    .map(deviceId -> "r.deviceId == \"" + deviceId + "\"")
                    .collect(Collectors.joining(" or "));
            condition = " and (" + condition + ")";
        }
        return condition;
    }
}
