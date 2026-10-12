package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaDevicePO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.converters.TrafficDetectionAreaPersistenceConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao.TrafficDetectionAreaMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TrafficDetectionAreaRepository extends ServiceImpl<TrafficDetectionAreaMapper, TrafficDetectionAreaPO> {

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    private final TrafficDetectionAreaVisualRepository trafficDetectionAreaVisualRepository;

    public TrafficDetectionAreaRepository(TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository,
                                          TrafficDetectionAreaVisualRepository trafficDetectionAreaVisualRepository
                                         ) {
        this.trafficDetectionAreaDeviceRepository = trafficDetectionAreaDeviceRepository;
        this.trafficDetectionAreaVisualRepository = trafficDetectionAreaVisualRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public TrafficDetectionArea save(TrafficDetectionArea trafficDetectionArea) {
        // 转换为PO
        TrafficDetectionAreaPO trafficDetectionAreaPO = TrafficDetectionAreaPersistenceConverter.toTrafficDetectionAreaPO(trafficDetectionArea);

        if (trafficDetectionArea.getId() == null) {
            // 新增
            baseMapper.insert(trafficDetectionAreaPO);
            trafficDetectionArea.setId(trafficDetectionAreaPO.getId());
        } else {
            var trafficDetectionAreaPOFromDB = this.baseMapper.selectById(trafficDetectionAreaPO.getId());
            trafficDetectionAreaPO.setDeleted(trafficDetectionAreaPOFromDB.getDeleted());
            trafficDetectionAreaPO.setCreateBy(trafficDetectionAreaPOFromDB.getCreateBy());
            trafficDetectionAreaPO.setCreateTime(trafficDetectionAreaPOFromDB.getCreateTime());
            // 更新
            baseMapper.updateById(trafficDetectionAreaPO);
            //删除关联视角
            trafficDetectionAreaVisualRepository.deleteById(trafficDetectionArea.getId());
        }
        // 保存视角配置
        if (Objects.nonNull(trafficDetectionArea.getViewConfig())) {
            trafficDetectionAreaVisualRepository.save(trafficDetectionArea.getId(), trafficDetectionArea.getViewConfig());
        }
        return trafficDetectionArea;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDevices(TrafficDetectionArea trafficDetectionArea) {
        //删除关联设备
        trafficDetectionAreaDeviceRepository.deleteByTrafficDetectionAreaId(trafficDetectionArea.getId());
        // 保存设备关联
        if (CollectionUtils.isNotEmpty(trafficDetectionArea.getDevices())) {
            trafficDetectionAreaDeviceRepository.saveBatch(trafficDetectionArea.getId(), trafficDetectionArea.getDevices());
        }
    }

    public TrafficDetectionArea findById(Long id) {
        // 查询人流区域基本信息
        var trafficDetectionAreaPO = baseMapper.selectById(id);
        if (trafficDetectionAreaPO == null) {
            return null;
        }
        // 查询视角配置
        var viewConfig = trafficDetectionAreaVisualRepository.findById(id);
        // 查询设备关联
        var devices = trafficDetectionAreaDeviceRepository.findListByTrafficDetectionAreaId(id);
        var trafficDetectionArea = TrafficDetectionAreaPersistenceConverter.toTrafficDetectionArea(trafficDetectionAreaPO);
        trafficDetectionArea.setViewConfig(viewConfig);
        trafficDetectionArea.setDevices(devices);
        return trafficDetectionArea;
    }

    /**
     * 查询列表
     */
    public List<TrafficDetectionArea> findList(Period period, String keyword) {
        var trafficDetectionAreaPOS = baseMapper.selectList(Wrappers
                                                                    .<TrafficDetectionAreaPO>lambdaQuery()
                                                                    .between(Objects.nonNull(period), TrafficDetectionAreaPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                    .like(StringUtils.isNotBlank(keyword), TrafficDetectionAreaPO::getName, keyword));
        if (CollectionUtils.isEmpty(trafficDetectionAreaPOS)) {
            return Collections.emptyList();
        }
        var trafficDetectionAreaIds = trafficDetectionAreaPOS.stream().map(TrafficDetectionAreaPO::getId).collect(Collectors.toList());
        var trafficDetectionAreaDevices = trafficDetectionAreaDeviceRepository.list(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                                            .in(!CollectionUtils.isEmpty(trafficDetectionAreaIds), TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        var deviceMap = trafficDetectionAreaDevices.stream()
                .collect(Collectors.groupingBy(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId));
        return trafficDetectionAreaPOS.stream()
                .map(trafficDetectionAreaPO -> TrafficDetectionAreaPersistenceConverter.toTrafficDetectionArea(trafficDetectionAreaPO, null, deviceMap.get(trafficDetectionAreaPO.getId())))
                .collect(Collectors.toList());
    }

    /**
     * 分页查询流量区域信息
     */
    public Pagination<TrafficDetectionArea> findPage(Paging paging, Period period, String keyword
                                                    ) {
        var trafficDetectionAreaPOPage = baseMapper.selectPage(PagingUtils.convert(paging), Wrappers
                .<TrafficDetectionAreaPO>lambdaQuery()
                .between(Objects.nonNull(period), TrafficDetectionAreaPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .like(StringUtils.isNotBlank(keyword), TrafficDetectionAreaPO::getName, keyword));
        var records = trafficDetectionAreaPOPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PagingUtils.convert(trafficDetectionAreaPOPage, Collections.emptyList());
        }
        var trafficDetectionAreaIds = records.stream().map(TrafficDetectionAreaPO::getId).collect(Collectors.toList());
        var trafficDetectionAreaDevices = trafficDetectionAreaDeviceRepository.list(Wrappers.<TrafficDetectionAreaDevicePO>lambdaQuery()
                                                                                            .in(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId, trafficDetectionAreaIds));
        var deviceMap = trafficDetectionAreaDevices.stream()
                .collect(Collectors.groupingBy(TrafficDetectionAreaDevicePO::getTrafficDetectionAreaId));
        // 转换为领域对象（与列表查询一致：批量加载设备关联，供人流量聚合等场景使用）
        var trafficDetectionAreas = records.stream()
                .map(trafficDetectionAreaPO -> TrafficDetectionAreaPersistenceConverter.toTrafficDetectionArea(trafficDetectionAreaPO, null, deviceMap.get(trafficDetectionAreaPO.getId())))
                .collect(Collectors.toList());
        return PagingUtils.convert(trafficDetectionAreaPOPage, trafficDetectionAreas);
    }

    public List<TrafficDetectionArea> findListByDeviceIds(Collection<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return CollectionUtils.emptyList();
        }
        var trafficDetectionAreaPOS = this.baseMapper.selectTrafficDetectionAreasByDeviceIds(deviceIds);
        return trafficDetectionAreaPOS.stream()
                .map(TrafficDetectionAreaPersistenceConverter::toTrafficDetectionArea)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<Long> ids) {
        var trafficDetectionAreaPOS = baseMapper.selectList(Wrappers.<TrafficDetectionAreaPO>lambdaQuery()
                                                                    .in(TrafficDetectionAreaPO::getId, ids));
        if (CollectionUtils.isNotEmpty(trafficDetectionAreaPOS)) {
            DatabaseContextHolder.softDelete(TrafficDetectionAreaPO.class, trafficDetectionAreaPOS);
            // 删除设备关联
            trafficDetectionAreaDeviceRepository.deleteByTrafficDetectionAreaId(ids);
            // 删除视角配置
            trafficDetectionAreaVisualRepository.deleteByIds(ids);
        }
    }
}
