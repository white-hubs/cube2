package com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.dao.VehicleGuideBoardConfigMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity.GuideContentConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity.VehicleGuideBoardConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.GuideContentConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.VehicleGuideBoardConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.vo.VehicleGuideBoardConfigVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.ParkingInfoCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingInfoQueryService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.converters.ParkingInfoConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 车辆引导牌配置表 Service 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleGuideBoardConfigServiceImpl extends ServiceImpl<VehicleGuideBoardConfigMapper, VehicleGuideBoardConfig> implements VehicleGuideBoardConfigService {
    private final static String GUIDE_SCENE_CODE = "vehicle_guide_board";
    private final GuideContentConfigService guideContentConfigService;
    private final FileOperateClient fileOperateClient;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;
    private final ParkingInfoQueryService parkingInfoQueryService;
    private final ParkingInfoCommandService parkingInfoCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(VehicleGuideBoardConfigParam param) {
        // 1. 保存引导牌配置
        var config = new VehicleGuideBoardConfig();
        config.setId(IdWorker.getId());
        config.setName(param.getName());
        config.setCode(param.getCode());
        config.setSceneImageIds(param.getSceneImageIds());
        config.setInstallationLocation(param.getInstallationLocation());
        config.setParkingLotId(param.getParkingLotId());
        config.setRemark(param.getRemark());
        this.save(config);

        // 1.1 持久化文件
        persistSceneImages(config.getId(), param.getSceneImageIds());

        // 2. 保存关联的内容配置
        if (CollectionUtils.isNotEmpty(param.getContentConfigs())) {
            var contentConfigs = new ArrayList<GuideContentConfig>();
            for (VehicleGuideBoardConfigParam.GuideContentConfigParam contentParam : param.getContentConfigs()) {
                var contentConfig = new GuideContentConfig();
                contentConfig.setId(IdWorker.getId());
                contentConfig.setGuideBoardConfigId(config.getId());
                contentConfig.setContentName(contentParam.getContentName());
                contentConfig.setPriority(contentParam.getPriority());
                contentConfig.setBoundField(contentParam.getBoundField());
                contentConfig.setFontSize(contentParam.getFontSize());
                contentConfig.setFontColor(contentParam.getFontColor());
                contentConfigs.add(contentConfig);
            }
            guideContentConfigService.saveBatch(contentConfigs);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(VehicleGuideBoardConfigParam param) {
        Assert.isParameterError(Objects.isNull(param.getId()), "更新时id不能为空");

        // 1. 更新引导牌配置
        var config = this.getById(param.getId());
        Assert.isNotFound(config, "引导牌配置不存在");
        config.setName(param.getName());
        config.setCode(param.getCode());
        config.setSceneImageIds(param.getSceneImageIds());
        config.setInstallationLocation(param.getInstallationLocation());
        config.setParkingLotId(param.getParkingLotId());
        config.setRemark(param.getRemark());
        this.updateById(config);

        // 1.1 处理文件持久化：先删除旧的，再持久化新的
        fileRefClient.removeByBusinessKey(param.getId());
        persistSceneImages(param.getId(), param.getSceneImageIds());

        // 2. 处理关联的内容配置
        // 2.1 查询现有的所有内容配置
        var existingContentConfigs = guideContentConfigService.list(
                Wrappers.<GuideContentConfig>lambdaQuery()
                        .eq(GuideContentConfig::getGuideBoardConfigId, param.getId())
                                                                   );
        var existingContentConfigsMap = MapUtils.listToMap(existingContentConfigs, GuideContentConfig::getId, guideContentConfig -> guideContentConfig);
        var existingIds = existingContentConfigs.stream()
                .map(GuideContentConfig::getId)
                .collect(Collectors.toSet());

        // 2.2 收集新列表中的id
        var newIds = new HashSet<Long>();
        if (CollectionUtils.isNotEmpty(param.getContentConfigs())) {
            for (VehicleGuideBoardConfigParam.GuideContentConfigParam contentParam : param.getContentConfigs()) {
                if (contentParam.getId() != null) {
                    newIds.add(contentParam.getId());
                }
            }
        }

        // 2.3 删除不在新列表中的内容配置
        var idsToDelete = existingIds.stream()
                .filter(id -> !newIds.contains(id))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idsToDelete)) {
            guideContentConfigService.removeByIds(idsToDelete);
        }

        // 2.4 更新已存在的内容配置和新增新的内容配置
        if (CollectionUtils.isNotEmpty(param.getContentConfigs())) {
            var contentConfigsToUpdate = new ArrayList<GuideContentConfig>();
            var contentConfigsToSave = new ArrayList<GuideContentConfig>();

            for (VehicleGuideBoardConfigParam.GuideContentConfigParam contentParam : param.getContentConfigs()) {

                if (contentParam.getId() != null && existingIds.contains(contentParam.getId())) {
                    // 更新已存在的内容配置
                    var contentConfig = existingContentConfigsMap.get(contentParam.getId());
                    contentConfig.setGuideBoardConfigId(param.getId());
                    contentConfig.setContentName(contentParam.getContentName());
                    contentConfig.setPriority(contentParam.getPriority());
                    contentConfig.setBoundField(contentParam.getBoundField());
                    contentConfig.setFontSize(contentParam.getFontSize());
                    contentConfig.setFontColor(contentParam.getFontColor());
                    contentConfigsToUpdate.add(contentConfig);
                } else {
                    // 新增新的内容配置
                    var contentConfig = new GuideContentConfig();
                    contentConfig.setId(IdWorker.getId());
                    contentConfig.setGuideBoardConfigId(param.getId());
                    contentConfig.setContentName(contentParam.getContentName());
                    contentConfig.setPriority(contentParam.getPriority());
                    contentConfig.setBoundField(contentParam.getBoundField());
                    contentConfig.setFontSize(contentParam.getFontSize());
                    contentConfig.setFontColor(contentParam.getFontColor());
                    contentConfigsToSave.add(contentConfig);
                }
            }

            if (CollectionUtils.isNotEmpty(contentConfigsToUpdate)) {
                guideContentConfigService.updateBatchById(contentConfigsToUpdate);
            }
            if (CollectionUtils.isNotEmpty(contentConfigsToSave)) {
                guideContentConfigService.saveBatch(contentConfigsToSave);
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        // 1. 删除文件持久化信息
        fileRefClient.removeByBusinessKey(id);

        // 2. 删除关联的内容配置
        guideContentConfigService.remove(
                Wrappers.<GuideContentConfig>lambdaQuery()
                        .eq(GuideContentConfig::getGuideBoardConfigId, id)
                                        );

        // 3. 删除引导牌配置
        return this.removeById(id);
    }

    @Override
    public VehicleGuideBoardConfigVO getDetail(Long id) {
        // 1. 查询引导牌配置
        var config = this.getById(id);
        Assert.isNotFound(config, "引导牌配置不存在");

        // 2. 查询关联的内容配置
        var contentConfigs = guideContentConfigService.list(
                Wrappers.<GuideContentConfig>lambdaQuery()
                        .eq(GuideContentConfig::getGuideBoardConfigId, id)
                        .orderByAsc(GuideContentConfig::getPriority)
                                                           );

        // 3. 获取文件信息
        var fileVOList = fileClient.getByBusinessKey(config.getId());

        // 4. 组装VO
        var vo = new VehicleGuideBoardConfigVO();
        vo.setId(config.getId());
        vo.setName(config.getName());
        vo.setCode(config.getCode());
        vo.setSceneImageIds(config.getSceneImageIds());
        vo.setInstallationLocation(config.getInstallationLocation());
        vo.setParkingLotId(config.getParkingLotId());
        var parkingInfo = parkingInfoQueryService.getParkingInfoById(config.getParkingLotId());
        var parkingInfoVO = ParkingInfoConverter.toParkingVO(parkingInfo);
        vo.setParkingInfo(parkingInfoVO);
        vo.setRemark(config.getRemark());
        vo.setSceneImageFiles(fileVOList);

        if (CollectionUtils.isNotEmpty(contentConfigs)) {
            var contentVOs = contentConfigs.stream()
                    .map(content -> {
                        var contentVO = new VehicleGuideBoardConfigVO.GuideContentConfigVO();
                        contentVO.setId(content.getId());
                        contentVO.setContentName(content.getContentName());
                        contentVO.setPriority(content.getPriority());
                        contentVO.setBoundField(content.getBoundField());
                        contentVO.setFontSize(content.getFontSize());
                        contentVO.setFontColor(content.getFontColor());
                        return contentVO;
                    })
                    .collect(Collectors.toList());
            vo.setContentConfigs(contentVOs);
        }

        return vo;
    }

    @Override
    public Pagination<VehicleGuideBoardConfigVO> page(VehicleGuideBoardConfigQueryParam param) {
        // 1. 分页查询引导牌配置
        var page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), VehicleGuideBoardConfig::getName, param.getName())
                .orderByDesc(VehicleGuideBoardConfig::getUpdateTime)
                .page(PagingUtils.convert(param));

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var parkingIds = ListUtils.convertItem(page.getRecords(), VehicleGuideBoardConfig::getParkingLotId);
        var parkingVOs = parkingInfoQueryService.getParkingInfoByIds(parkingIds);
        var parkingInfoMap = MapUtils.listToMap(parkingVOs, ParkingInfoVO::getId, parkingInfoVO -> parkingInfoVO);
        // 2. 转换为VO
        var voList = page.getRecords().stream()
                .map(config -> {
                    var vo = new VehicleGuideBoardConfigVO();
                    vo.setId(config.getId());
                    vo.setName(config.getName());
                    vo.setCode(config.getCode());
                    vo.setSceneImageIds(config.getSceneImageIds());
                    vo.setInstallationLocation(config.getInstallationLocation());
                    vo.setParkingLotId(config.getParkingLotId());
                    if (vo.getParkingLotId() != null) {
                        vo.setParkingInfo(parkingInfoMap.get(vo.getParkingLotId()));
                    }
                    vo.setRemark(config.getRemark());
                    return vo;
                })
                .collect(Collectors.toList());

        return PagingUtils.convert(page, voList);
    }

    /**
     * 持久化现场图片文件
     *
     * @param businessKey   业务键（引导牌配置id）
     * @param sceneImageIds 现场图片文件id（多个，逗号分隔）
     */
    private void persistSceneImages(Long businessKey, String sceneImageIds) {
        if (StringUtils.isBlank(sceneImageIds)) {
            return;
        }

        // 将逗号分隔的字符串转换为文件id列表
        var fileIds = Arrays.stream(sceneImageIds.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(fileIds)) {
            var fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdList(fileIds);
            fileInduceParam.setBusinessKey(businessKey);
            fileInduceParam.setSceneCode(GUIDE_SCENE_CODE);
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
    }

}

