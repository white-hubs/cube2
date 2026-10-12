package com.guhecloud.cube.biz.daily.task.domain.adapter.service.impl;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetAreaClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetDeviceClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetPatrolClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetPointClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetPropertyClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetRiskClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetSmallClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetStallClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.AreaDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.PatrolLocationVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.ProjectScheduleVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.PropertyDetailedVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.RiskSourceLedgerVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.ScenePointVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.StallDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.AreaQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DailyTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DevicePostByIdSetParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.InspectScenePointQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.PatrolLocationPageParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.ProjectScheduleQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.PropertyDetailedByIdListParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.RiskSourceLedgerBatchParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.StallMasterOtherParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.StallPostQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.service.DailyTargetQueryService;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 执行对象查询服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTargetQueryServiceImpl implements DailyTargetQueryService {
    private final DailyTargetDeviceClient dailyTargetDeviceClient;
    private final DailyTargetStallClient dailyTargetStallClient;
    private final DailyTargetPointClient dailyTargetPointClient;
    private final DailyTargetPatrolClient dailyTargetPatrolClient;
    private final DailyTargetPropertyClient dailyTargetPropertyClient;
    private final DailyTargetRiskClient dailyTargetRiskClient;
    private final DailyTargetSmallClient dailyTargetSmallClient;
    private final DailyTargetAreaClient dailyTargetAreaClient;

    /**
     * 批量查询对象信息
     *
     * @param param 对象id集合
     *
     * @return 对象集合
     */
    @Override
    public Map<Long, DailyTargetDTO> postDailyTargetList(DailyTargetQueryParam param) {
        if (StringUtils.isBlank(param.getTargetCategory()) || CollectionUtils.isEmpty(param.getTargetIdSet())) {
            return Collections.emptyMap();
        }
        if (TaskTargetTypeEnum.DEVICE.name().equals(param.getTargetCategory())) {
            var list = dailyTargetDeviceClient.postDetailsListByDeviceId(DevicePostByIdSetParam.builder()
                                                                                 .deviceIdSet(param.getTargetIdSet()).build());
            return handlerDeviceResult(list);
        } else if (TaskTargetTypeEnum.STALL.name().equals(param.getTargetCategory())) {
            var list = dailyTargetStallClient.postStallListByIds(StallPostQueryParam.builder()
                                                                         .stallIdList(new ArrayList<>(param.getTargetIdSet())).build());
            return handlerStallResult(list);
        } else if (TaskTargetTypeEnum.SITE_POINT.name().equals(param.getTargetCategory())) {
            var list = dailyTargetPointClient.scenePointListBatch(InspectScenePointQueryParam.builder()
                                                                          .ids(param.getTargetIdSet())
                                                                          .build());
            return handlerPointResult(list);
        } else if (TaskTargetTypeEnum.PROPERTY.name().equals(param.getTargetCategory())) {
            var list = dailyTargetPropertyClient.queryPropertyDetailBatchPost(PropertyDetailedByIdListParam
                                                                                      .builder()
                                                                                      .idList(new ArrayList<>(param.getTargetIdSet()))
                                                                                      .build());
            return handlerPropertyResult(list);
        } else if (TaskTargetTypeEnum.PATROL_POINT.name().equals(param.getTargetCategory())) {
            var list = dailyTargetPatrolClient.queryLocationBatch(PatrolLocationPageParam.builder().idSet(param.getTargetIdSet()).build());
            return handlerPatrolResult(list);
        } else if (TaskTargetTypeEnum.RISK_SOURCE.name().equals(param.getTargetCategory())) {
            var list = dailyTargetRiskClient.queryRiskSourceLedgerBatchPost(RiskSourceLedgerBatchParam.builder()
                                                                                    .idList(new ArrayList<>(param.getTargetIdSet()))
                                                                                    .build());
            return handlerRiskResult(list);
        } else if (TaskTargetTypeEnum.SMALL_ENGINEERING.name().equals(param.getTargetCategory())) {
            var list = dailyTargetSmallClient.projectScheduleBatchPost(ProjectScheduleQueryParam.builder()
                                                                               .projectIds(new ArrayList<>(param.getTargetIdSet()))
                                                                               .build());
            return handlerSmallResult(list);
        } else if (TaskTargetTypeEnum.AREA.name().equals(param.getTargetCategory())) {
            var list = dailyTargetAreaClient.listInnerAreaPost(AreaQueryParam.builder().ids(param.getTargetIdSet()).build());
            return handlerAreaResult(list);
        }
        return Collections.emptyMap();
    }

    /**
     * 获取某档位档主的其他档位
     * 注意不包含自己
     *
     * @param targetId   档位id
     * @param otherIdSet 其他档位id列表
     *
     * @return 其他档位列表
     */
    @Override
    public List<DailyTargetDTO> postStallMasterOthers(Long targetId, Set<Long> otherIdSet) {
        var stallList = dailyTargetStallClient.postStallMasterOthers(StallMasterOtherParam.builder().id(targetId)
                                                                             .otherIdList(otherIdSet).build());
        return stallList.stream().map(stall -> DailyTargetDTO.builder().id(stall.getId()).targetCategory(TaskTargetTypeEnum.STALL.name())
                .targetCode(stall.getCode()).targetName(stall.getName()).location(stall.getLocation())
                .areaId(stall.getAreaId()).areaName(stall.getAreaName()).build()).collect(Collectors.toList());
    }

    /**
     * 处理设备列表结果，将设备基础信息转换为每日目标DTO映射
     *
     * @param deviceList 设备基础信息列表
     *
     * @return 以设备ID为键，每日目标DTO为值的映射表
     */
    private Map<Long, DailyTargetDTO> handlerDeviceResult(List<DeviceBaseDetailsDTO> deviceList) {
        if (CollectionUtils.isEmpty(deviceList)) {
            return Collections.emptyMap();
        }
        // 将设备基础信息转换为每日目标DTO，并以设备ID为键构建映射表
        return deviceList.stream()
                .map(device -> DailyTargetDTO.builder().id(device.getId()).targetCategory(TaskTargetTypeEnum.DEVICE.name())
                        .targetCode(device.getDeviceCode()).targetName(device.getDeviceName()).location(device.getLocationDes())
                        .areaId(device.getAreaId()).areaName(device.getAreaName()).build())
                .collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理档位结果，将其转换为每日目标DTO映射
     *
     * @param stallList 档位DTO列表
     *
     * @return 以档位ID为键，每日目标DTO为值的映射表
     */
    private Map<Long, DailyTargetDTO> handlerStallResult(List<StallDTO> stallList) {
        if (CollectionUtils.isEmpty(stallList)) {
            return Collections.emptyMap();
        }
        // 将档位DTO列表转换为每日目标DTO映射，使用档位ID作为键
        return stallList.stream()
                .map(stall -> DailyTargetDTO.builder().id(stall.getId()).targetCategory(TaskTargetTypeEnum.STALL.name())
                        .targetCode(stall.getCode()).targetName(stall.getName()).areaId(stall.getAreaId()).areaName(stall.getAreaName())
                        .location(stall.getLocation())
                        .lntAndLat(stall.getStallExpand() == null ? null : stall.getStallExpand().getLntAndLat()).build())
                .collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理巡更点结果，将ScenePointVO列表转换为DailyTargetDTO映射
     *
     * @param pointList 巡更点信息列表
     *
     * @return 以ID为键，DailyTargetDTO为值的映射表，如果输入列表为空则返回空映射
     */
    private Map<Long, DailyTargetDTO> handlerPointResult(List<ScenePointVO> pointList) {
        if (CollectionUtils.isEmpty(pointList)) {
            return Collections.emptyMap();
        }
        // 将ScenePointVO列表转换为DailyTargetDTO映射，使用ID作为键，处理重复ID时保留第一个元素
        return pointList.stream()
                .map(point -> DailyTargetDTO.builder().id(point.getId()).targetCategory(TaskTargetTypeEnum.SITE_POINT.name())
                        .targetCode(point.getCodeNum()).targetName(point.getName()).areaId(point.getAreaId()).areaName(point.getAreaName())
                        .location(point.getLocation())
                        .lntAndLat(point.getLatAndLon()).build()
                    ).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理资产结果，将其转换为DailyTargetDTO映射
     *
     * @param propertyList 属性详细信息列表
     *
     * @return 以ID为键，DailyTargetDTO对象为值的映射表
     */
    private Map<Long, DailyTargetDTO> handlerPropertyResult(List<PropertyDetailedVO> propertyList) {
        if (CollectionUtils.isEmpty(propertyList)) {
            return Collections.emptyMap();
        }

        // 将资产转换为DailyTargetDTO映射
        return propertyList.stream()
                .map(property -> {
                    var areaIdBuilder = new StringBuilder();
                    var areaNameBuilder = new StringBuilder();

                    // 构建区域ID和名称字符串，用"-"分隔
                    Optional.ofNullable(property.getArea()).orElse(Collections.emptyList()).forEach(area -> {
                        areaIdBuilder.append(area.getId()).append("-");
                        areaNameBuilder.append(area.getName()).append("-");
                    });

                    // 移除末尾的"-"分隔符
                    var areaId = !areaIdBuilder.isEmpty() ? areaIdBuilder.substring(0, areaIdBuilder.length() - 1) : null;
                    var areaName = !areaNameBuilder.isEmpty() ? areaNameBuilder.substring(0, areaNameBuilder.length() - 1) : null;

                    // 构建DailyTargetDTO对象
                    return DailyTargetDTO.builder().id(property.getId()).targetCategory(TaskTargetTypeEnum.PROPERTY.name())
                            .targetCode(property.getSerialNumber()).targetName(property.getName()).areaId(areaId).areaName(areaName)
                            .location(property.getLocation())
                            .lntAndLat(property.getLatAndLon()).build();
                }).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理巡检结果，将巡检位置列表转换为每日目标DTO映射
     *
     * @param patrolList 巡检位置列表
     *
     * @return 以ID为键，DailyTargetDTO为值的映射表，如果输入列表为空则返回空映射
     */
    private Map<Long, DailyTargetDTO> handlerPatrolResult(List<PatrolLocationVO> patrolList) {
        if (CollectionUtils.isEmpty(patrolList)) {
            return Collections.emptyMap();
        }
        // 将巡检位置列表转换为每日目标DTO映射，使用ID作为键，处理重复ID时保留第一个值
        return patrolList.stream()
                .map(patrol -> DailyTargetDTO.builder().id(patrol.getId()).targetCategory(TaskTargetTypeEnum.PATROL_POINT.name())
                        .targetName(patrol.getPatrolLocationName()).areaId(patrol.getAreaId()).areaName(patrol.getAreaName())
                        .lntAndLat(patrol.getPatrolLongitude() + "," + patrol.getPatrolDimension()).build()
                    ).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理风险源数据，将其转换为日常目标DTO映射
     *
     * @param riskList 风险源台账列表数据
     *
     * @return 以ID为键，DailyTargetDTO为值的映射表，如果输入列表为空则返回空映射
     */
    private Map<Long, DailyTargetDTO> handlerRiskResult(List<RiskSourceLedgerVO> riskList) {
        if (CollectionUtils.isEmpty(riskList)) {
            return Collections.emptyMap();
        }
        // 将风险源数据转换为日常目标DTO，并以ID为键构建映射表
        return riskList.stream()
                .map(risk -> DailyTargetDTO.builder().id(risk.getId()).targetCategory(TaskTargetTypeEnum.RISK_SOURCE.name())
                        .targetCode(risk.getSerialNumber()).targetName(risk.getName()).areaId(risk.getAreaId()).areaName(risk.getAreaName())
                        .location(risk.getLocation()).lntAndLat(risk.getLatAndLon()).build()
                    ).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理小散工程结果列表，将其转换为DailyTargetDTO映射
     *
     * @param smallList 小散工程调度信息列表
     *
     * @return 以ID为键，DailyTargetDTO对象为值的映射表
     */
    private Map<Long, DailyTargetDTO> handlerSmallResult(List<ProjectScheduleVO> smallList) {
        if (CollectionUtils.isEmpty(smallList)) {
            return Collections.emptyMap();
        }
        // 将ProjectScheduleVO列表转换为DailyTargetDTO映射
        return smallList.stream()
                .map(small -> DailyTargetDTO.builder().id(small.getId()).targetCategory(TaskTargetTypeEnum.SMALL_ENGINEERING.name())
                        .targetCode(small.getProjectNum()).targetName(small.getProjectName()).areaId(small.getAreaId())
                        .areaName(small.getAreaName()).location(small.getAddress()).build()
                    ).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

    /**
     * 处理区域结果，将区域列表转换为每日目标DTO映射
     *
     * @param areaList 区域DTO列表
     *
     * @return 以ID为键，每日目标DTO为值的映射表，如果输入列表为空则返回空映射
     */
    private Map<Long, DailyTargetDTO> handlerAreaResult(List<AreaDTO> areaList) {
        if (CollectionUtils.isEmpty(areaList)) {
            return Collections.emptyMap();
        }
        // 将区域DTO列表转换为每日目标DTO映射，使用区域ID作为键
        return areaList.stream()
                .map(area -> DailyTargetDTO.builder().id(area.getId()).targetCategory(TaskTargetTypeEnum.AREA.name())
                        .targetCode(area.getCode()).targetName(area.getName()).build()
                    ).collect(Collectors.toMap(DailyTargetDTO::getId, dailyTargetDTO -> dailyTargetDTO, (o, n) -> o));
    }

}
