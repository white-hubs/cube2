package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters.ParkingInfoPersistenceConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingInfoMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
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
public class ParkingInfoRepository extends ServiceImpl<ParkingInfoMapper, ParkingInfoPO> {

    private final ParkingInfoDeviceRepository parkingInfoDeviceRepository;

    private final ParkingInfoVisualRepository parkingInfoVisualRepository;

    private final ParkingInfoDockConfigRepository parkingInfoDockConfigRepository;

    public ParkingInfoRepository(ParkingInfoDeviceRepository parkingInfoDeviceRepository,
                                 ParkingInfoVisualRepository parkingInfoVisualRepository,
                                 ParkingInfoDockConfigRepository parkingInfoDockConfigRepository
                                ) {
        this.parkingInfoDeviceRepository = parkingInfoDeviceRepository;
        this.parkingInfoVisualRepository = parkingInfoVisualRepository;
        this.parkingInfoDockConfigRepository = parkingInfoDockConfigRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ParkingInfo save(ParkingInfo parkingInfo) {
        // 转换为PO
        ParkingInfoPO parkingInfoPO = ParkingInfoPersistenceConverter.toParkingInfoPO(parkingInfo);

        if (parkingInfo.getId() == null) {
            // 新增
            baseMapper.insert(parkingInfoPO);
            parkingInfo.setId(parkingInfoPO.getId());
        } else {
            var parkingInfoPOFromDB = this.baseMapper.selectById(parkingInfoPO.getId());
            parkingInfoPO.setDeleted(parkingInfoPOFromDB.getDeleted());
            EntityAuditor.populateCreateAudit(parkingInfoPO,
                                              parkingInfoPOFromDB.getCreateBy(),
                                              parkingInfoPOFromDB.getCreatorId(),
                                              parkingInfoPOFromDB.getCreatorType(),
                                              parkingInfoPOFromDB.getCreateTime());
            // 更新
            baseMapper.updateById(parkingInfoPO);
            //删除关联视角
            parkingInfoVisualRepository.deleteById(parkingInfo.getId());
            //删除关联设备
            parkingInfoDeviceRepository.deleteByParkingId(parkingInfo.getId());
            //删除关联三方配置
            parkingInfoDockConfigRepository.deleteById(parkingInfo.getId());
        }
        // 保存视角配置
        if (Objects.nonNull(parkingInfo.getViewConfig())) {
            parkingInfoVisualRepository.save(parkingInfo.getId(), parkingInfo.getViewConfig());
        }
        // 保存设备关联
        if (CollectionUtils.isNotEmpty(parkingInfo.getDevices())) {
            parkingInfoDeviceRepository.saveBatch(parkingInfo.getId(), parkingInfo.getDevices());
        }
        //保存三方配置关联
        if (Objects.nonNull(parkingInfo.getDockConfig())) {
            parkingInfoDockConfigRepository.save(parkingInfo.getId(), parkingInfo.getDockConfig());
        }
        return parkingInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public ParkingInfo updateParkingNumber(ParkingInfo parkingInfo) {
        // 转换为PO
        this.baseMapper.update(Wrappers.<ParkingInfoPO>lambdaUpdate()
                                       .set(ParkingInfoPO::getTotalInNum, parkingInfo.getTotalInNum())
                                       .set(ParkingInfoPO::getTotalSpaceNum, parkingInfo.getTotalSpaceNum())
                                       .eq(ParkingInfoPO::getId, parkingInfo.getId()));
        return parkingInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delAndSaveBatchThirdParking(@NonNull List<ParkingInfoPO> parkingInfos) {
        this.baseMapper.delete(Wrappers.<ParkingInfoPO>lambdaQuery().and(q -> q
                .eq(ParkingInfoPO::getSource, ParkingSourceEnum.THIRD)
                .or()
                .isNull(ParkingInfoPO::getSource)));
        for (ParkingInfoPO parkingInfo : parkingInfos) {
            parkingInfo.setSource(ParkingSourceEnum.THIRD);
        }
        this.saveBatch(parkingInfos);
    }

    public ParkingInfo findById(Long id) {
        // 查询车场基本信息
        var parkingInfoPO = baseMapper.selectById(id);
        if (parkingInfoPO == null) {
            return null;
        }
        // 查询视角配置
        var parkingViewConfig = parkingInfoVisualRepository.findById(id);
        // 查询设备关联
        var devices = parkingInfoDeviceRepository.findListByParkingId(id);
        var parkingInfo = ParkingInfoPersistenceConverter.toParkingInfo(parkingInfoPO);
        // 查询三方配置关联
        var dockConfig = parkingInfoDockConfigRepository.findById(id);
        parkingInfo.setDockConfig(dockConfig);
        parkingInfo.setViewConfig(parkingViewConfig);
        parkingInfo.setDevices(devices);
        return parkingInfo;
    }

    /**
     * 查询车场列表
     */
    public List<ParkingInfo> findList(Collection<String> parkingInfoCodes, String source) {
        var parkingInfoPOs = baseMapper.selectList(Wrappers.<ParkingInfoPO>lambdaQuery()
                                                           .in(!CollectionUtils.isEmpty(parkingInfoCodes), ParkingInfoPO::getCode, parkingInfoCodes)
                                                           .eq(StringUtils.isNotBlank(source), ParkingInfoPO::getSource, source));
        if (CollectionUtils.isEmpty(parkingInfoPOs)) {
            return Collections.emptyList();
        }
        var parkingIds = parkingInfoPOs.stream().map(ParkingInfoPO::getId).collect(Collectors.toList());
        var parkingDevices = parkingInfoDeviceRepository.list(Wrappers.<ParkingInfoDevicePO>lambdaQuery()
                                                                      .in(!CollectionUtils.isEmpty(parkingIds), ParkingInfoDevicePO::getParkingInfoId, parkingIds));
        var parkingDeviceMap = parkingDevices.stream().collect(Collectors.groupingBy(ParkingInfoDevicePO::getParkingInfoId));
        return parkingInfoPOs.stream()
                .map(parking -> ParkingInfoPersistenceConverter.toParkingInfo(parking, null, parkingDeviceMap.get(parking.getId())))
                .collect(Collectors.toList());
    }

    /**
     * 分页查询车场信息
     */
    public Pagination<ParkingInfo> findPage(Paging paging, Collection<String> parkingInfoCodes,
                                            String source, String keyword
                                           ) {
        var parkingInfoPage = baseMapper.selectPage(PagingUtils.convert(paging), Wrappers
                .<ParkingInfoPO>lambdaQuery()
                .in(!CollectionUtils.isEmpty(parkingInfoCodes), ParkingInfoPO::getCode, parkingInfoCodes)
                .eq(StringUtils.isNotBlank(source), ParkingInfoPO::getSource, source)
                .and(StringUtils.isNotBlank(keyword), t -> t
                        .like(ParkingInfoPO::getName, keyword)
                        .or().like(ParkingInfoPO::getCode, keyword)));
        // 转换为领域对象
        var parkingInfos = parkingInfoPage.getRecords().stream()
                .map(ParkingInfoPersistenceConverter::toParkingInfo)
                .collect(Collectors.toList());
        return PagingUtils.convert(parkingInfoPage, parkingInfos);
    }

    public Pagination<ParkingInfo> findHasDockConfigPage(Paging paging, String source, String keyword, String providerCode) {
        var parkingInfoPage = this.baseMapper.selectDockConfigParkingInfoPage(PagingUtils.convert(paging), source, keyword, providerCode);
        // 转换为领域对象
        var parkingInfos = parkingInfoPage.getRecords().stream()
                .map(ParkingInfoPersistenceConverter::toParkingInfo)
                .collect(Collectors.toList());
        return PagingUtils.convert(parkingInfoPage, parkingInfos);
    }

    public List<ParkingInfo> findHasDockConfigsByProviderCode(String providerCode) {
        var parkingInfoPage = this.baseMapper.selectDockConfigsParkingInfoByProviderCode(providerCode);
        // 转换为领域对象
        return parkingInfoPage.stream()
                .map(ParkingInfoPersistenceConverter::toParkingInfo)
                .collect(Collectors.toList());
    }

    public List<ParkingInfoDevice> findDevicesByProviderCode(String providerCode) {
        var parkingInfoDevicePOS = this.baseMapper.selectParkingInfoDevicesByProviderCode(providerCode);
        // 转换为领域对象
        return ParkingInfoPersistenceConverter.toParkingInfoDevices(parkingInfoDevicePOS);
    }

    public List<ParkingInfo> findListByDeviceIds(Collection<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return CollectionUtils.emptyList();
        }
        var parkingInfoPOS = this.baseMapper.selectParkingInfosByDeviceIds(deviceIds);
        return parkingInfoPOS.stream()
                .map(ParkingInfoPersistenceConverter::toParkingInfo)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<Long> ids) {
        var parkingInfoPOS = baseMapper.selectList(Wrappers.<ParkingInfoPO>lambdaQuery().in(ParkingInfoPO::getId, ids));
        if (CollectionUtils.isNotEmpty(parkingInfoPOS)) {
            DatabaseContextHolder.softDelete(ParkingInfoPO.class, parkingInfoPOS);
            // 删除设备关联
            parkingInfoDeviceRepository.deleteByParkingIds(ids);
            // 删除视角配置
            parkingInfoVisualRepository.deleteByIds(ids);
        }
    }
}
