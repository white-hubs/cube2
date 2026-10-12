package com.guhecloud.cube.base.device.center.visualization.service;

import com.guhecloud.cube.base.device.center.visualization.param.DeviceUsefulLifeExpireParam;
import com.guhecloud.cube.base.device.center.visualization.param.VisualizationQueryParam;
import com.guhecloud.cube.base.device.center.visualization.vo.AreaEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireChainVO;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireVO;
import com.guhecloud.cube.base.device.center.visualization.vo.OverallEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.StatusEquipmentStatisticsVo;
import com.guhecloud.platform.base.model.Pagination;

public interface DeviceCenterVisualizationService {
    OverallEquipmentStatisticsVo getOverallEquipmentStatistics(VisualizationQueryParam param);

    StatusEquipmentStatisticsVo getStatusEquipmentStatistics(VisualizationQueryParam param);

    AreaEquipmentStatisticsVo getAreaEquipmentStatistics(VisualizationQueryParam param);

    Pagination<DeviceUsefulLifeExpireVO> getUsefulLifeExpiredDevicePage(DeviceUsefulLifeExpireParam param);

    DeviceUsefulLifeExpireChainVO getDeviceUsefulLifeExpireChainVO(DeviceUsefulLifeExpireParam param);

}
