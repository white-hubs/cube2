package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaDevicePO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.converters.TrafficDetectionAreaPersistenceConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao.TrafficDetectionAreaDeviceMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TrafficDetectionAreaDeviceRepository extends ServiceImpl<TrafficDetectionAreaDeviceMapper, TrafficDetectionAreaDevicePO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Long trafficDetectionAreaId, List<TrafficDetectionAreaDevice> trafficDetectionAreaDevices) {
        this.saveBatch(trafficDetectionAreaDevices
                               .stream()
                               .map(device -> TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaDevicePO(device, trafficDetectionAreaId))
                               .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long trafficDetectionAreaId, TrafficDetectionAreaDevice trafficDetectionAreaDevice) {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .eq(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaId)
                                                                               .eq(TrafficDetectionAreaDevicePO::getDeviceId, trafficDetectionAreaDevice.getDeviceId()));
        if (CollectionUtils.isEmpty(trafficDetectionAreaDevicePOS)) {
            return;
        }
        for (var trafficDetectionAreaDevicePO : trafficDetectionAreaDevicePOS) {
            trafficDetectionAreaDevicePO.setDeviceConfig(trafficDetectionAreaDevice.getDeviceConfig());
        }
        this.updateBatchById(trafficDetectionAreaDevicePOS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTrafficDetectionAreaId(Long trafficDetectionAreaId) {
        deleteByTrafficDetectionAreaId(Collections.singleton(trafficDetectionAreaId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTrafficDetectionAreaId(Collection<Long> trafficDetectionAreaIds) {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .in(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        if (CollectionUtils.isNotEmpty(trafficDetectionAreaDevicePOS)) {
            DatabaseContextHolder.softDelete(TrafficDetectionAreaDevicePO.class, trafficDetectionAreaDevicePOS);
        }
    }

    public List<TrafficDetectionAreaDevice> findListByTrafficDetectionAreaId(Long trafficDetectionAreaId) {
        return findListByTrafficDetectionAreaIds(Collections.singleton(trafficDetectionAreaId));
    }

    public List<TrafficDetectionAreaDevice> findListByTrafficDetectionAreaIds(Collection<Long> trafficDetectionAreaIds) {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .in(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        return TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaDevices(trafficDetectionAreaDevicePOS);
    }

    public List<TrafficDetectionAreaDevice> findByDeviceIds(Collection<Long> deviceIds) {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .in(TrafficDetectionAreaDevicePO::getDeviceId, deviceIds));
        return TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaDevices(trafficDetectionAreaDevicePOS);
    }

    public List<TrafficDetectionAreaDevice> findAll() {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(null);
        return TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaDevices(trafficDetectionAreaDevicePOS);
    }

    public List<TrafficDetectionAreaDevicePO> findAllPO() {
        return this.baseMapper.selectList(null);
    }

    public List<TrafficDetectionAreaDevice> findList(Collection<Long> trafficDetectionAreaIds) {
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .in(CollectionUtils.isNotEmpty(trafficDetectionAreaIds), TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        return TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaDevices(trafficDetectionAreaDevicePOS);
    }

    /**
     * 按区域分组设备 id（用于批量汇总区域人流等场景）
     */
    public Map<Long, List<Long>> mapTrafficDetectionAreaIdToDeviceIds(Collection<Long> trafficDetectionAreaIds) {
        if (CollectionUtils.isEmpty(trafficDetectionAreaIds)) {
            return Collections.emptyMap();
        }
        var trafficDetectionAreaDevicePOS = this.baseMapper.selectList(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                               .in(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        if (CollectionUtils.isEmpty(trafficDetectionAreaDevicePOS)) {
            return Collections.emptyMap();
        }
        return trafficDetectionAreaDevicePOS.stream()
                .collect(Collectors.groupingBy(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId,
                                               Collectors.mapping(TrafficDetectionAreaDevicePO::getDeviceId, Collectors.toList())));
    }
}
