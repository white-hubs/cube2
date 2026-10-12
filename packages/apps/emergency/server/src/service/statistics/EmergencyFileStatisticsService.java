package com.guhecloud.cube.biz.emergency.server.service.statistics;

import com.guhecloud.cube.biz.emergency.server.param.statistics.EmergencyFileStatisticsParam;
import com.guhecloud.cube.biz.emergency.server.vo.statistics.EmergencyFileStatisticsVO;

public interface EmergencyFileStatisticsService {

    EmergencyFileStatisticsVO getEmergencyFileStatistics(EmergencyFileStatisticsParam param);
}
