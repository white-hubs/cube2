package com.guhecloud.cube.biz.vmonitor.server.param.statistics;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import lombok.Data;

@Data
public class CameraPatrolLineStatisticsParam extends CameraPatrolLineStatisticsQueryParam {

    /**
     * 线路启用状态
     */
    private Boolean enabled;
}
