package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaVisualPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.converters.TrafficDetectionAreaPersistenceConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao.TrafficDetectionAreaVisualMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Repository
public class TrafficDetectionAreaVisualRepository extends ServiceImpl<TrafficDetectionAreaVisualMapper, TrafficDetectionAreaVisualPO> {

    @Transactional(rollbackFor = Exception.class)
    public void save(Long trafficDetectionAreaId, TrafficDetectionAreaVisual trafficDetectionAreaVisual) {
        var trafficDetectionAreaVisualPO = TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaVisualPO(trafficDetectionAreaVisual, trafficDetectionAreaId);
        this.saveOrUpdate(trafficDetectionAreaVisualPO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long trafficDetectionAreaId) {
        deleteByIds(Collections.singleton(trafficDetectionAreaId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<Long> trafficDetectionAreaVisualIds) {
        var trafficDetectionAreaVisualPOS = baseMapper.selectList(Wrappers.<TrafficDetectionAreaVisualPO>lambdaQuery()
                                                                          .in(TrafficDetectionAreaVisualPO::getId, trafficDetectionAreaVisualIds));
        if (!CollectionUtils.isEmpty(trafficDetectionAreaVisualPOS)) {
            this.removeByIds(trafficDetectionAreaVisualIds);
        }
    }

    public TrafficDetectionAreaVisual findById(Long trafficDetectionAreaVisualId) {
        var trafficDetectionAreaVisualPO = this.getById(trafficDetectionAreaVisualId);
        if (Objects.isNull(trafficDetectionAreaVisualPO)) {
            return null;
        }
        return TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaVisual(trafficDetectionAreaVisualPO);
    }
}
