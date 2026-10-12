package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FireFightingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.FireFightingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireFightingDeviceStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCategoryCountVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 消防设备统计
 * @date 2023/9/8 13:58
 */
public interface FireFightingDeviceStatisticsService {
    FireFightingDeviceStatisticsVO geFireFightingDeviceStatistics(BaseQuery query);

    Pagination<FireFightingDeviceVO> getFireFightingDeviceList(FireFightingDeviceQuery fireFightingDevice);

    List<String> getDeviceTypeList();

    Long countDeviceTotalNum(List<String> list);

    List<DeviceCategoryCountVO> getDeviceCategoryCount(FireFightingDeviceQuery fireFightingDevice);
}
