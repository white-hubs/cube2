package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsEventGroupVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsHandlerVO;

import java.util.List;

public interface AlarmBimEventStatisticsService {

    List<AlarmBimStatisticsHandlerVO> getEventTypeHandlerListStatistics(AlarmBimStatisticsQueryParam param);

    List<AlarmBimStatisticsHandlerVO> getEventTypeListStatistics(AlarmStatisticsQueryParam param);

    List<AlarmBimStatisticsEventGroupVO> getEventTypeGroupTotalStatistics(AlarmBimStatisticsQueryParam param);
}
