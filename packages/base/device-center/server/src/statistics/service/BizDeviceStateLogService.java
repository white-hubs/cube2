package com.guhecloud.cube.base.device.center.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStateLog;

/**
 * @author wql
 * @describe 设备状态变更记录
 * @date 2024/1/22 16:53
 */
public interface BizDeviceStateLogService extends IService<BizDeviceStateLog> {
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
     * 统计设备离线次数
     *
     * @param id 设备id
     *
     * @return 离线次数
     */
    Integer countOffLineNum(Long id);
}
