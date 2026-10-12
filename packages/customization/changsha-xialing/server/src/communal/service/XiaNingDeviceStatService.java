package com.guhecloud.cube.customization.xialing.server.communal.service;

import com.guhecloud.cube.customization.xialing.server.communal.vo.DeviceStatisticsVO;

public interface XiaNingDeviceStatService {

    /**
     * 统计配置产品范围内的设备总数、在线数和离线数。
     *
     * @return 设备统计结果
     */
    DeviceStatisticsVO statistics();
}
