package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_UTC_FORMAT;

/**
 * @author Appet
 * @Description 能耗采集时段定义
 * @date 2024-01-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadLogParam extends Paging {

    //仪表类型
    private String instrumentType;
    //时间段
    private Period period;
    //区域ID 多个逗号分隔
    private String areaIds;
    //设备ID 多个逗号分隔
    private String deviceIds;
    // 设备code查询
    private String deviceKeyword;

    private String startTime;

    private String endTime;

    public void initInfluxdbPeriod() {
        if (period != null) {
            startTime = DateUtils.formatDate(DateUtils.offset(period.getBegin(), ChronoUnit.HOURS, -8), DEFAULT_UTC_FORMAT);
            endTime = DateUtils.formatDate(DateUtils.offset(period.getEnd(), ChronoUnit.HOURS, -8), DEFAULT_UTC_FORMAT);
        } else {
            LocalDateTime start = DateUtils.beginOfDay(LocalDateTime.now());
            LocalDateTime end = DateUtils.endOfDay(LocalDateTime.now());
            startTime = DateUtils.formatDate(DateUtils.offset(start, ChronoUnit.HOURS, -8), DEFAULT_UTC_FORMAT);
            endTime = DateUtils.formatDate(DateUtils.offset(end, ChronoUnit.HOURS, -8), DEFAULT_UTC_FORMAT);
        }
    }

}