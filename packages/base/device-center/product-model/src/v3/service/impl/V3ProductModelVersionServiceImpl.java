package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.constant.ModelConstants;
import com.guhecloud.cube.base.iot.product.model.v3.enums.OperationTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.dto.ProductModelSnapshotDTO;
import com.guhecloud.cube.base.iot.product.model.v3.event.VersionRollbackEvent;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductEventMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMethodMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductModelVersionMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductPropMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictItemMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProduct;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductModelVersion;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductEventService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SystemDictCacheService;
import com.guhecloud.cube.base.iot.product.model.v3.util.DictCodeAndName;
import com.guhecloud.cube.base.iot.product.model.v3.util.ProductModelSnapshotConverter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductModelVersionVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物模型版本管理Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3ProductModelVersionServiceImpl extends ServiceImpl<V3MdProductModelVersionMapper, MdProductModelVersion> implements V3ProductModelVersionService {

    private final V3MdProductMapper productMapper;
    private final V3MdProductPropMapper propMapper;
    private final V3MdProductEventMapper eventMapper;
    private final V3MdProductMethodMapper methodMapper;
    private final V3DictItemMapper sysDictItemMapper;
    private final ObjectMapper objectMapper;
    private final V3SystemDictCacheService sysDictCacheService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSnapshot(Long prodId, String changeType, String changeDesc) {
        log.info("开始创建版本快照, prodId={}, changeType={}", prodId, changeType);

        // 1. 查询产品信息
        MdProduct product = productMapper.selectById(prodId);
        if (product == null) {
            throw new InvalidArgumentException("产品不存在: " + prodId);
        }

        // 2. 查询物模型的所有组成部分
        List<ProductPropVO> props = propMapper.selectByProdId(prodId);
        List<ProductEventVO> events = eventMapper.selectByProdIdNoCategoryName(prodId);
        List<ProductMethodVO> methods = methodMapper.selectByProdId(prodId);

        // 2.5. 封装字典项信息对象
        DictCodeAndName categoryInfo = null;
        if (product.getProdCategoryId() != null) {
            categoryInfo = sysDictItemMapper.selectDictCodeAndNameById(product.getProdCategoryId());
        }

        DictCodeAndName brandInfo = null;
        if (product.getProdBrandId() != null) {
            brandInfo = sysDictItemMapper.selectDictCodeAndNameById(product.getProdBrandId());
        }

        DictCodeAndName protocolInfo = null;
        if (product.getProtocolId() != null) {
            protocolInfo = sysDictItemMapper.noTenantSelectDictCodeAndNameById(product.getProtocolId());
        }

        // 2.3.2 填充事件分类名称
        for (ProductEventVO event : events) {
            event.setEventCategoryName(sysDictCacheService.getDictItemLabel(event.getEventCategoryId()));
        }

        // 3. 构建完整的物模型快照(使用标准DTO)
        ProductModelSnapshotDTO snapshot = ProductModelSnapshotConverter.buildCompleteSnapshot(
                product, categoryInfo, brandInfo, protocolInfo, props, events, methods, false);

        String snapshotJson;
        try {
            snapshotJson = objectMapper.writeValueAsString(snapshot);
        } catch (Exception e) {
            throw new InvalidArgumentException("序列化物模型快照失败", e);
        }

        // 4. 获取当前的最新版本号
        MdProductModelVersion latestVersion = getCurrentVersion(prodId);
        String newVersionNumber = generateVersionNumber(latestVersion);

        // 5. 将旧版本标记为非当前版本
        if (latestVersion != null) {
            latestVersion.setIsCurrent(0);
            updateById(latestVersion);
        }

        // 6. 创建新版本记录
        MdProductModelVersion version = new MdProductModelVersion();
        version.setProdId(prodId);
        version.setProdNumber(product.getProdNumber());
        version.setVersionNumber(newVersionNumber);
        version.setVersionName("版本 " + newVersionNumber);
        version.setModelSnapshot(snapshotJson);
        version.setChangeType(changeType);
        version.setChangeDescription(changeDesc);
        version.setIsCurrent(1);
        version.setStatus(1);

        // 生成变更摘要
        if (latestVersion != null && OperationTypeEnum.UPDATE.getCode().equals(changeType)) {
            String changeSummary = generateChangeSummary(latestVersion.getModelSnapshot(), snapshotJson);
            version.setChangeSummary(changeSummary);
        }

        save(version);

        log.info("版本快照创建成功, versionId={}, versionNumber={}", version.getId(), newVersionNumber);
        return version.getId();
    }

    @Override
    public List<ProductModelVersionVO> listVersions(Long prodId) {
        log.info("查询产品版本列表, prodId={}", prodId);

        LambdaQueryWrapper<MdProductModelVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdProductModelVersion::getProdId, prodId)
                .eq(MdProductModelVersion::getStatus, 1)
                .orderByDesc(MdProductModelVersion::getCreateTime);

        List<MdProductModelVersion> versions = list(wrapper);

        return versions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public ProductModelVersionVO getVersionDetail(Long versionId) {
        log.info("查询版本详情, versionId={}", versionId);

        MdProductModelVersion version = getById(versionId);
        if (version == null) {
            throw new RuntimeException("版本不存在: " + versionId);
        }

        return convertToVO(version);
    }

    @Override
    public Map<String, Object> compareVersions(Long versionId1, Long versionId2) {
        log.info("对比版本差异, versionId1={}, versionId2={}", versionId1, versionId2);

        MdProductModelVersion version1 = getById(versionId1);
        MdProductModelVersion version2 = getById(versionId2);

        if (version1 == null || version2 == null) {
            throw new RuntimeException("版本不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("version1", convertToVO(version1));
        result.put("version2", convertToVO(version2));

        try {
            ProductModelSnapshotDTO snapshot1 = objectMapper.readValue(
                    version1.getModelSnapshot(), ProductModelSnapshotDTO.class);
            ProductModelSnapshotDTO snapshot2 = objectMapper.readValue(
                    version2.getModelSnapshot(), ProductModelSnapshotDTO.class);

            // 对比差异
            Map<String, Object> diff = new HashMap<>();
            diff.put("properties", compareListSize(snapshot1.getProperties(), snapshot2.getProperties()));
            diff.put("events", compareListSize(snapshot1.getEvents(), snapshot2.getEvents()));
            diff.put("methods", compareListSize(snapshot1.getMethods(), snapshot2.getMethods()));

            result.put("diff", diff);
        } catch (Exception e) {
            log.error("解析版本快照失败", e);
            throw new RuntimeException("解析版本快照失败", e);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackToVersion(Long prodId, Long versionId) {
        log.info("开始回滚版本, prodId={}, versionId={}", prodId, versionId);

        // 1. 获取目标版本
        MdProductModelVersion targetVersion = getById(versionId);
        if (targetVersion == null) {
            throw new RuntimeException("目标版本不存在: " + versionId);
        }

        if (!targetVersion.getProdId().equals(prodId)) {
            throw new RuntimeException("版本与产品不匹配");
        }

//        // 2. 解析目标版本的快照
//        ProductModelSnapshotDTO snapshot;
//        try {
//            snapshot = objectMapper.readValue(
//                    targetVersion.getModelSnapshot(), ProductModelSnapshotDTO.class);
//        } catch (Exception e) {
//            throw new RuntimeException("解析版本快照失败", e);
//        }

        // 3. 发布版本回滚事件(触发数据恢复操作)
        eventPublisher.publishEvent(VersionRollbackEvent.builder()
                .prodId(prodId)
                .targetVersionId(versionId)
                .snapshotData(targetVersion.getModelSnapshot())
                .build());

        // 4. 创建回滚版本快照
//        String changeDesc = "回滚到版本 " + targetVersion.getVersionNumber();
//        createSnapshot(prodId, OperationTypeEnum.ROLLBACK.getCode(), changeDesc);

        log.info("版本回滚成功, prodId={}, targetVersion={}", prodId, targetVersion.getVersionNumber());
    }

    @Override
    public MdProductModelVersion getCurrentVersion(Long prodId) {
        LambdaQueryWrapper<MdProductModelVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdProductModelVersion::getProdId, prodId)
                .eq(MdProductModelVersion::getIsCurrent, 1)
                .eq(MdProductModelVersion::getStatus, 1)
                .orderByDesc(MdProductModelVersion::getCreateTime)
                .last("LIMIT 1");

        return getOne(wrapper);
    }

    /**
     * 生成新版本号
     */
    private String generateVersionNumber(MdProductModelVersion latestVersion) {
        if (latestVersion == null) {
            return "v1.0.0";
        }

        String currentVersion = latestVersion.getVersionNumber();
        if (currentVersion == null || !currentVersion.startsWith("v")) {
            return "v1.0.0";
        }

        // 解析版本号并递增
        String[] parts = currentVersion.substring(1).split("\\.");
        if (parts.length != 3) {
            return "v1.0.0";
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);

            // 递增补丁版本号
            patch++;
            return String.format("v%d.%d.%d", major, minor, patch);
        } catch (NumberFormatException e) {
            return "v1.0.0";
        }
    }

    /**
     * 生成变更摘要
     */
    private String generateChangeSummary(String oldSnapshotJson, String newSnapshotJson) {
        try {
            ProductModelSnapshotDTO oldSnapshot = objectMapper.readValue(
                    oldSnapshotJson, ProductModelSnapshotDTO.class);
            ProductModelSnapshotDTO newSnapshot = objectMapper.readValue(
                    newSnapshotJson, ProductModelSnapshotDTO.class);

            Map<String, Object> summary = new HashMap<>();

            // 简单地比较数量变化
            List<?> oldProps = oldSnapshot.getProperties();
            List<?> newProps = newSnapshot.getProperties();
            if (oldProps != null && newProps != null && oldProps.size() != newProps.size()) {
                summary.put("properties", newProps.size() - oldProps.size());
            }

            List<?> oldEvents = oldSnapshot.getEvents();
            List<?> newEvents = newSnapshot.getEvents();
            if (oldEvents != null && newEvents != null && oldEvents.size() != newEvents.size()) {
                summary.put("events", newEvents.size() - oldEvents.size());
            }

            List<?> oldMethods = oldSnapshot.getMethods();
            List<?> newMethods = newSnapshot.getMethods();
            if (oldMethods != null && newMethods != null && oldMethods.size() != newMethods.size()) {
                summary.put("methods", newMethods.size() - oldMethods.size());
            }

            return objectMapper.writeValueAsString(summary);
        } catch (Exception e) {
            log.error("生成变更摘要失败", e);
            return "{}";
        }
    }

    /**
     * 对比两个列表的差异(简化版,仅比较数量)
     */
    private Map<String, Object> compareListSize(List<?> list1, List<?> list2) {
        Map<String, Object> result = new HashMap<>();
        result.put("count1", list1 != null ? list1.size() : 0);
        result.put("count2", list2 != null ? list2.size() : 0);
        result.put("diff", (list2 != null ? list2.size() : 0) - (list1 != null ? list1.size() : 0));
        return result;
    }

    /**
     * 转换为VO对象(显式设置字段,避免隐式拷贝)
     */
    private ProductModelVersionVO convertToVO(MdProductModelVersion version) {
        ProductModelVersionVO vo = new ProductModelVersionVO();
        vo.setId(version.getId());
        vo.setProdId(version.getProdId());
        vo.setProdNumber(version.getProdNumber());
        vo.setVersionNumber(parseVersionNumber(version.getVersionNumber()));
        vo.setVersionTag(version.getVersionNumber()); // 原始版本号字符串
        vo.setVersionDesc(version.getChangeDescription());
        vo.setModelSnapshot(version.getModelSnapshot());
        vo.setChangeType(version.getChangeType());
        vo.setChangeSummary(version.getChangeSummary());
        vo.setIsActive(version.getIsCurrent());
        vo.setCreateTime(version.getCreateTime());
        vo.setCreateBy(version.getCreateBy());
        return vo;
    }

    /**
     * 解析版本号为整数(用于VO展示)
     */
    private Integer parseVersionNumber(String versionNumber) {
        if (versionNumber == null || !versionNumber.startsWith("v")) {
            return 1;
        }
        String[] parts = versionNumber.substring(1).split("\\.");
        if (parts.length != 3) {
            return 1;
        }
        try {
            return Integer.parseInt(parts[0]) * 10000 + Integer.parseInt(parts[1]) * 100 + Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
