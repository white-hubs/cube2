package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingHourlyVehicleFlowPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingHourlyVehicleFlowMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public class ParkingHourlyVehicleFlowRepository
        extends ServiceImpl<ParkingHourlyVehicleFlowMapper, ParkingHourlyVehicleFlowPO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveRecords(List<ParkingHourlyVehicleFlowPO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        this.saveBatch(records);
    }

    public boolean hasRecords() {
        return this.count() > 0;
    }

    public List<ParkingHourlyVehicleFlowPO> findByDeviceIdsAndRecordTimeBetween(Collection<Long> deviceIds,
                                                                                LocalDateTime begin,
                                                                                LocalDateTime end) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return List.of();
        }
        return this.baseMapper.selectList(Wrappers.<ParkingHourlyVehicleFlowPO>lambdaQuery()
                                                  .in(ParkingHourlyVehicleFlowPO::getDeviceId, deviceIds)
                                                  .ge(begin != null, ParkingHourlyVehicleFlowPO::getRecordTime, begin)
                                                  .lt(end != null, ParkingHourlyVehicleFlowPO::getRecordTime, end)
                                                  .orderByAsc(ParkingHourlyVehicleFlowPO::getRecordTime));
    }
}
