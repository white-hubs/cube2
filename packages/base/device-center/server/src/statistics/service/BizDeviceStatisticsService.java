package com.guhecloud.cube.base.device.center.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStatistics;

import java.util.Map;
import java.util.Set;

public interface BizDeviceStatisticsService extends IService<BizDeviceStatistics> {
    /**
     * 保存设备状态更新记录
     *
     * @param iotDeviceId 设备id
     * @param value       状态值
     */
    void saveStateLog(Long iotDeviceId, int value);

    /**
     * 统计设备在线时长
     *
     * @param id 设备id
     *
     * @return 在线时长(小时)
     */
    Long countOnlineTime(Long id);

    /**
     * 设备统计信息获取
     *
     * @param ids 设备id集合
     *
     * @return 设备统计信息
     */
    Map<Long, BizDeviceStatistics> deviceStatistics(Set<Long> ids);

    /**
     * 统计设备离线次数
     *
     * @param id 设备id
     *
     * @return 离线次数
     */
    Integer countOffLineNum(Long id);
}
