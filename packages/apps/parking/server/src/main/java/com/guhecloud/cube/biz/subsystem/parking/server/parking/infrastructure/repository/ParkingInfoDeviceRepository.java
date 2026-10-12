package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters.ParkingInfoPersistenceConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingInfoDeviceMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ParkingInfoDeviceRepository extends ServiceImpl<ParkingInfoDeviceMapper, ParkingInfoDevicePO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Long parkingInfoId, List<ParkingInfoDevice> parkingInfoDevices) {
        this.saveBatch(parkingInfoDevices
                               .stream().map(device -> ParkingInfoPersistenceConverter.toParkingInfoDevicePO(device, parkingInfoId))
                               .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateByDevices(List<ParkingInfoDevice> parkingInfoDevices) {
        for (ParkingInfoDevice device : parkingInfoDevices) {
            this.update(Wrappers.lambdaUpdate(ParkingInfoDevicePO.class)
                                .set(ParkingInfoDevicePO::getCurrentCarNumber, device.getCurrentCarNumber())
                                .eq(ParkingInfoDevicePO::getDeviceId, device.getDeviceId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByParkingId(Long parkingId) {
        deleteByParkingIds(Collections.singleton(parkingId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByParkingIds(Collection<Long> parkingIds) {
        var parkingInfoDevicePOS = this.baseMapper.selectList(Wrappers.<ParkingInfoDevicePO>lambdaQuery()
                                                                      .in(ParkingInfoDevicePO::getParkingInfoId, parkingIds));
        if (CollectionUtils.isNotEmpty(parkingInfoDevicePOS)) {
            DatabaseContextHolder.softDelete(ParkingInfoDevicePO.class, parkingInfoDevicePOS);
        }
    }

    public List<ParkingInfoDevice> findListByParkingId(Long parkingId) {
        return findListByParkingIds(Collections.singleton(parkingId));
    }

    public List<ParkingInfoDevice> findListByParkingIds(Collection<Long> parkingIds) {
        var parkingInfoDevicePOS = this.baseMapper.selectList(Wrappers.<ParkingInfoDevicePO>lambdaQuery()
                                                                      .in(ParkingInfoDevicePO::getParkingInfoId, parkingIds));
        return ParkingInfoPersistenceConverter.toParkingInfoDevices(parkingInfoDevicePOS);
    }

    public List<ParkingInfoDevice> findByDeviceIds(Collection<Long> deviceIds) {
        var parkingInfoDevicePOS = this.baseMapper.selectList(Wrappers.<ParkingInfoDevicePO>lambdaQuery()
                                                                      .in(ParkingInfoDevicePO::getDeviceId, deviceIds));
        return ParkingInfoPersistenceConverter.toParkingInfoDevices(parkingInfoDevicePOS);
    }

    public List<ParkingInfoDevice> findAll() {
        var parkingInfoDevicePOS = this.baseMapper.selectList(null);
        return ParkingInfoPersistenceConverter.toParkingInfoDevices(parkingInfoDevicePOS);
    }

    public List<ParkingInfoDevicePO> findAllPO() {
        return this.baseMapper.selectList(null);
    }
}
