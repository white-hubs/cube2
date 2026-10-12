package com.guhecloud.cube.biz.vmonitor.server.param.statistics;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import lombok.Data;

@Data
public class CameraPatrolTrendStatisticsQueryParam extends CameraPatrolLineStatisticsQueryParam {

    /**
     * mysql日期格式
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
     * 巡逻结果
     */
    private String patrolResult;
}
