package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.device.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCountStatusVO;

import java.util.List;

/**
 * 设备相关统计服务
 */
public interface DeviceStatisticsService {
    /**
     * 根据应用种类统计设备状态数
     *
     * @param query
     *
     * @return
     */
    List<DeviceCountStatusVO> countStatusByApplication(DeviceApplicationStatisticsQuery query);
}
