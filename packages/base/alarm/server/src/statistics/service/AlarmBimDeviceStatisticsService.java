package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsActualVO;

import java.util.List;

public interface AlarmBimDeviceStatisticsService {

    /**
     * 根据设备标签分组统计报警正确率
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsActualVO> getDeviceTagActualRatioStatistics(AlarmBimStatisticsQueryParam param);
}
