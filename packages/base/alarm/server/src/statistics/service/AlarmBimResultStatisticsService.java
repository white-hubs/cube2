package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimHandlerResultStatisticsVO;

public interface AlarmBimResultStatisticsService {

    AlarmBimHandlerResultStatisticsVO getAlarmHandlerResultStatistics(AlarmBimStatisticsQueryParam param);

}
