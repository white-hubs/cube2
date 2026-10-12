package com.guhecloud.cube.biz.vmonitor.server.param.statistics;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import lombok.Data;

@Data
public class CameraPatrolTopStatisticsQueryParam extends CameraPatrolLineStatisticsQueryParam {

    /**
     * top大小
     */
    private Integer topSize;
}
