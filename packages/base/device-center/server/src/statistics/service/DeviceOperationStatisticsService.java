package com.guhecloud.cube.base.device.center.statistics.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.base.device.center.statistics.param.DeviceStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceMaintainStatisticsVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsNumVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:48
 */
public interface DeviceOperationStatisticsService {
    Pagination<DeviceStatisticsVO> deviceStatistics(DeviceStatisticsQueryParam query);

    Pagination<DeviceMaintainStatisticsVO> deviceMaintainStatistics(DeviceStatisticsQueryParam query);

    ExcelWriter deviceStatisticsExport(DeviceStatisticsQueryParam query);

    ExcelWriter deviceMaintainStatisticsExport(DeviceStatisticsQueryParam query);

    List<DeviceStatisticsNumVO> getDevicesNumber(String period);
}
