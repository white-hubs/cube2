package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;

import java.util.List;

public interface AlarmBimDeptStatisticsService {

    List<AlarmBimStatisticsTopChainVO> getDeptHandlerChainTopStatistics(AlarmBimStatisticsQueryParam param);
}
