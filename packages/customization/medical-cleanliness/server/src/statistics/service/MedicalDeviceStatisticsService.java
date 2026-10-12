package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalDeviceStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalDeviceVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:43
 * @Description:
 * @Version: 1.0
 */
public interface MedicalDeviceStatisticsService {
    Boolean addMedicalDevice(List<MedicalDeviceStatistics> params);

    Pagination<MedicalDeviceVO> getMedicalDevicePage(MedicalDeviceParam param);
}
