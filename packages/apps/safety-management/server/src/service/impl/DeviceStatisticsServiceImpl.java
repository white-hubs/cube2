package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.DeviceManageMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.device.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.DeviceManagePO;
import com.guhecloud.cube.srv.gen.safety.management.service.DeviceStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "safetyDeviceStatisticsService")
public class DeviceStatisticsServiceImpl extends ServiceImpl<DeviceManageMapper, DeviceManagePO> implements DeviceStatisticsService {

    private final DeviceManageMapper deviceManageMapper;

    public DeviceStatisticsServiceImpl(DeviceManageMapper mapper) {
        this.deviceManageMapper = mapper;
    }

    @Override
    public List<DeviceCountStatusVO> countStatusByApplication(DeviceApplicationStatisticsQuery query) {
        return deviceManageMapper.countStatusByApplication(query, query.getApplicationCodes());
    }
}
