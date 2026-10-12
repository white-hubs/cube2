package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalDeviceHistoryStatisticsParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalDeviceHistoryStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalOverrunAlarmRecordVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalRecordStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalOverrunAlarmRecordParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-09  18:54
 * @Description:
 * @Version: 1.0
 */
public interface MedicalOverrunRecordService {
    /**设备历史监测记录分页*/
    Pagination<MedicalOverrunAlarmRecordVO> medicalOverrunAlarmPage(MedicalOverrunAlarmRecordParam param);
    Boolean deleteRecord(List<Long> id);
    /**
     * 设备历史监测记录统计
     */
    Pagination<MedicalRecordStatisticsVO> medicalDeviceStatistics();
    /**设备历史监测记录状态统计*/
    Pagination<MedicalDeviceHistoryStatisticsVO> deviceHistoryStatistics(MedicalDeviceHistoryStatisticsParam param);

}
