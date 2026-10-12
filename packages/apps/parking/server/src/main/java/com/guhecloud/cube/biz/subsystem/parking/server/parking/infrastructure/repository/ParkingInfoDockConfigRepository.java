package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDockConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters.ParkingInfoPersistenceConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingInfoDockConfigMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Repository
public class ParkingInfoDockConfigRepository extends ServiceImpl<ParkingInfoDockConfigMapper, ParkingInfoDockConfigPO> {

    @Transactional(rollbackFor = Exception.class)
    public void save(Long parkingInfoId, ParkingInfoDockConfig parkingInfoDockConfig) {
        var parkingInfoDockConfigPO = ParkingInfoPersistenceConverter.toParkingInfoDockConfigPO(parkingInfoDockConfig, parkingInfoId);
        this.save(parkingInfoDockConfigPO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long parkingInfoId) {
        deleteByIds(Collections.singleton(parkingInfoId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<Long> parkingIds) {
        var parkingInfoDockConfigPOS = baseMapper.selectList(Wrappers.<ParkingInfoDockConfigPO>lambdaQuery()
                                                                     .in(ParkingInfoDockConfigPO::getId, parkingIds));
        if (!CollectionUtils.isEmpty(parkingInfoDockConfigPOS)) {
            this.removeByIds(parkingIds);
        }
    }

    public ParkingInfoDockConfig findById(Long parkingInfoId) {
        var parkingInfoDockConfigPO = this.getById(parkingInfoId);
        if (Objects.isNull(parkingInfoDockConfigPO)) {
            return null;
        }
        return ParkingInfoPersistenceConverter.toParkingInfoDockConfig(parkingInfoDockConfigPO);
    }
}
