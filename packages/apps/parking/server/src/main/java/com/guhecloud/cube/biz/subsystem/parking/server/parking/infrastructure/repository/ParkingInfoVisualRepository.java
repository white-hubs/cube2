package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoVisualPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters.ParkingInfoPersistenceConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingInfoVisualMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Repository
public class ParkingInfoVisualRepository extends ServiceImpl<ParkingInfoVisualMapper, ParkingInfoVisualPO> {

    @Transactional(rollbackFor = Exception.class)
    public void save(Long parkingInfoId, ParkingInfoVisual parkingInfoVisual) {
        var parkingInfoVisualPO = ParkingInfoPersistenceConverter.toParkingInfoVisualPO(parkingInfoVisual, parkingInfoId);
        this.saveOrUpdate(parkingInfoVisualPO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long parkingInfoId) {
        deleteByIds(Collections.singleton(parkingInfoId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<Long> parkingIds) {
        var parkingInfoVisualPOS = baseMapper.selectList(Wrappers.<ParkingInfoVisualPO>lambdaQuery()
                                                                 .in(ParkingInfoVisualPO::getId, parkingIds));
        if (!CollectionUtils.isEmpty(parkingInfoVisualPOS)) {
            this.removeByIds(parkingIds);
        }
    }

    public ParkingInfoVisual findById(Long parkingInfoId) {
        var parkingInfoVisualPO = this.getById(parkingInfoId);
        if (Objects.isNull(parkingInfoVisualPO)) {
            return null;
        }
        return ParkingInfoPersistenceConverter.toParkingInfoVisual(parkingInfoVisualPO);
    }
}
