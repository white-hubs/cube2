package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalAlarmStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalAlarmVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:45
 * @Description:
 * @Version: 1.0
 */
public interface MedicalAlarmStatisticsService {
    Boolean addMedicalAlarm(List<MedicalAlarmStatistics> params);
    Pagination<MedicalAlarmVO> getMedicalAlarmPage(MedicalDeviceParam param);
}
