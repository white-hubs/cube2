package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class ParkingStatisticsQueryParam extends ParkingBaseQueryParam {

    /**
     * 时间类型：DAY、MONTH、YEAR、TODAY、THIS_MONTH、THIS_YEAR 等
     */
    private String type;

    /**
     * MySQL 日期格式
     */
    private String dateExp;

    /**
     * 格式化格式
     */
    private String dateString;

    /**
     * 展示的日期格式
     */
    private String viewDateString;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 统计颗粒度：HOUR、DAY、MONTH、YEAR
     */
    private String granularity;

    /**
     * 排序字段：TOTAL 累计车辆，AVG 时均车辆
     */
    private String sortField;
}
