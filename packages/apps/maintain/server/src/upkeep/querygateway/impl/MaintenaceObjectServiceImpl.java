package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenaceObjectService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 描述：维保对象服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenaceObjectServiceImpl extends ServiceImpl<MaintenanceObjectMapper, MaintenanceObject> implements MaintenaceObjectService {

    private final MaintenancePlanMapper maintenancePlanMapper;

    @Override
    public Integer isUpkeepValid(String deviceId, Period period) {
        if (Objects.nonNull(period)) {
            return maintenancePlanMapper.isUpkeepTaskValid(deviceId, DateUtils.format(period.getBegin()), DateUtils.format(period.getEnd()));
        } else {
            return maintenancePlanMapper.isUpkeepValid(deviceId);
        }

    }
}