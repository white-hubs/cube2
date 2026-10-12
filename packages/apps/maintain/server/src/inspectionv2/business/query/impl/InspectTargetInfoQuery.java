package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.param.StallPostQueryParam;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedByIdListParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerBatchParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl.AreaClientService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardTypeReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("OptionalOfRedundantMethod")
public class InspectTargetInfoQuery {

    private final StallClient stallClient;
    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;
    private final PropertyDetailedClient propertyDetailedClient;
    private final InspectStandardTypeReferQueryService standardTypeReferQueryService;
    private final InspectScenePointService inspectScenePointService;
    private final AreaClientService areaClientService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final ProjectScheduleClient projectScheduleClient;
    private final ApplicationClient applicationClient;

    /**
     * @Author wql
     * @Description 查询所有对象总数
     * @Date 15:44 2023/6/6
     **/
    public long getAllObjTotalNum() {
        // 资产总数
        long propertyCount = BigDecimal.ZERO.longValue();
        // 小散工程总数
        long projectScheduleCount = BigDecimal.ZERO.longValue();
        List<ApplicationDTO> applicationList = applicationClient.getTenantApplications(SessionContextHolder.getRequester().getTenant());
        Map<String, String> applicationMap = new HashMap<>();
        Optional.of(applicationList).ifPresent(list -> list.forEach(a -> applicationMap.put(a.getCode(), a.getName())));
        if (applicationMap.containsKey("PROPERTY_MANAGEMENT")) {
            //资产总数
            propertyCount = getPropertyTotalNum();
        }
        if (applicationMap.containsKey("SMALLENFINEERING")) {
            projectScheduleCount = projectScheduleClient.projectScheduleCount();
        }
        //档位总数
        List<StallDTO> stalls = Optional.of(stallClient.getStallList()).orElse(Collections.emptyList());
        //设备总数
        DeviceStateCountDTO stateCountDTO = deviceBaseStatisticsClient.countingNumberOfGroupByStatusFromSecurity(Collections.emptyList());
        //现场点总数
        long pointCount = inspectScenePointService.count();
        return pointCount + stalls.size() + stateCountDTO.getTotalNumber() + propertyCount + projectScheduleCount;
    }

    /**
     * @Author wql
     * @Description 获取资产对象总数
     * @Date 15:44 2023/6/6
     **/
    private long getPropertyTotalNum() {
        try {
            PropertyDetailedPageParam pageParam = new PropertyDetailedPageParam();
            pageParam.setPageSize(5);
            pageParam.setPageNumber(1);
            Pagination<PropertyDetailedPageVO> page = propertyDetailedClient.getPropertyDetailedPage(pageParam);
            return page.getTotal();
        } catch (Exception e) {
            log.error("获取资产总数异常:", e);
        }
        return 0L;
    }

    /**
     * @Author wql
     * @Description 设置资产对象信息
     * @Date 15:17 2023/6/6
     **/
    private InspectTargetInfoDTO setPropertyInfo(PropertyDetailedVO property) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(property)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.PROPERTY.name());
            vo.setTargetId(property.getId());
            vo.setTargetCode(property.getFinanceNumber());
            vo.setTargetName(property.getName());
            vo.setTargetType(String.valueOf(property.getTypeId()));
            vo.setTargetTypeName(property.getTypeName());
            vo.setAreaId(jointAreaId(property.getArea()));
            vo.setAreaName(jointName(property.getArea()));
            vo.setAddress(property.getStorageLocation());
            vo.setLocation(property.getLocation());
            vo.setLatAndLon(property.getLatAndLon());
        }
        return vo;
    }

    /**
     * @Author chx
     * @Description 设置风险源对象信息
     * @Date 15:17 2024/6/4
     **/
    private InspectTargetInfoDTO setRiskSourceInfo(RiskSourceLedgerVO riskSource) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(riskSource)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.RISK_SOURCE.name());
            vo.setTargetId(riskSource.getId());
            vo.setTargetCode(riskSource.getSerialNumber());
            vo.setTargetName(riskSource.getName());
            if (StringUtils.isNotBlank(riskSource.getTypeCode())) {
                String[] types = riskSource.getTypeCode().split(",");
                if (types.length > 0) {
                    vo.setTargetType(types[types.length - 1]);
                }
            }
            vo.setTargetTypeName(riskSource.getTypeName());
            vo.setAreaId(StringUtils.isNotBlank(riskSource.getAreaId()) ? riskSource.getAreaId().replaceAll(",", "-") : "");
            vo.setAreaName(StringUtils.isNotBlank(riskSource.getAreaName()) ? riskSource.getAreaName().replaceAll("/", "-") : "");
            vo.setAddress(riskSource.getAttribute());
            vo.setLocation(riskSource.getLocation());
            vo.setLatAndLon(riskSource.getLatAndLon());
        }
        return vo;
    }

    /**
     * @Author wql
     * @Description 设置设备信息
     * @Date 15:26 2023/6/6
     **/
    private InspectTargetInfoDTO setDeviceInfo(DeviceBaseDetailsDTO device) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(device)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.DEVICE.name());
            vo.setTargetId(device.getId());
            vo.setTargetCode(device.getDeviceCode());
            vo.setTargetName(device.getDeviceName());
            vo.setAreaId(device.getAreaId());
            vo.setAreaName(device.getAreaName());
            vo.setTargetType(device.getProductCategoryCode());
            vo.setTargetTypeName(device.getProductCategoryName());
            vo.setAddress(device.getLocationDes());
            vo.setLatAndLon(device.getLntAndLat());
        }
        return vo;
    }

    /**
     * @Author wql
     * @Description 设置档位信息
     * @Date 15:32 2023/6/6
     **/
    private InspectTargetInfoDTO setStallInfo(StallDTO stall) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(stall)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.STALL.name());
            vo.setTargetId(stall.getId());
            vo.setTargetCode(stall.getCode());
            vo.setTargetName(stall.getName());
            vo.setTargetType(stall.getType());
            vo.setTargetTypeName(stall.getTypeName());
            vo.setAreaId(stall.getArea());
            vo.setAreaName(stall.getAreaName());
            var stallExpandDTO = stall.getStallExpand();
            if (stallExpandDTO != null) {
                vo.setAddress(stallExpandDTO.getLocationDes());
                vo.setLatAndLon(stallExpandDTO.getLntAndLat());
            }

        }
        return vo;
    }

    /**
     * @Author wql
     * @Description 设置现场点对象信息
     * @Date 15:36 2023/6/6
     **/
    private InspectTargetInfoDTO setFieldPointInfo(InspectScenePointPO point) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(point)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.SITE_POINT.name());
            vo.setTargetId(point.getId());
            vo.setTargetCode(point.getCodeNum());
            vo.setTargetName(point.getName());
            vo.setTargetType(String.valueOf(point.getTypeId()));
            vo.setTargetTypeName(point.getTypeName());
            vo.setAreaId(String.valueOf(point.getAreaId()));
            vo.setAreaName(areaClientService.getAreaNamesByIds(point.getAreaId()));
            vo.setAddress(point.getLocation());
            vo.setLatAndLon(point.getLatAndLon());
        }
        return vo;
    }

    public List<InspectTargetInfoDTO> getInspectObject(String category, List<Long> targetIds, Map<Long, String> targetIdStandardIdsMap) {
        List<InspectTargetInfoDTO> objects = Collections.emptyList();
        if (InspectTargetCategoryEnum.DEVICE.name().equals(category)) {
            DevicePostByIdSetParam param = new DevicePostByIdSetParam();
            param.setDeviceIdSet(new HashSet<>(targetIds));
            List<DeviceBaseDetailsDTO> detailsListByDeviceId = deviceCenterStdClient.postDetailsListByDeviceId(param);
            objects = detailsListByDeviceId.stream().map(detailsDTO -> {
                InspectTargetInfoDTO vo = setDeviceInfo(detailsDTO);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        } else if (InspectTargetCategoryEnum.STALL.name().equals(category)) {
            StallPostQueryParam param = new StallPostQueryParam();
            param.setStallIdList(targetIds);
            List<StallDTO> stalls = stallClient.postStallListByIds(param);
            objects = stalls.stream().map(stall -> {
                InspectTargetInfoDTO vo = setStallInfo(stall);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        } else if (InspectTargetCategoryEnum.SITE_POINT.name().equals(category)) {
            List<InspectScenePointPO> pos = inspectScenePointService.listByIds(targetIds);
            objects = pos.stream().map(point -> {
                InspectTargetInfoDTO vo = setFieldPointInfo(point);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        } else if (InspectTargetCategoryEnum.PROPERTY.name().equals(category)) {
            PropertyDetailedByIdListParam param = new PropertyDetailedByIdListParam();
            param.setIdList(targetIds);
            List<PropertyDetailedVO> propertyList = propertyDetailedClient.queryPropertyDetailBatchPost(param);
            objects = propertyList.stream().map(property -> {
                InspectTargetInfoDTO vo = setPropertyInfo(property);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        } else if (InspectTargetCategoryEnum.RISK_SOURCE.name().equals(category)) {
            RiskSourceLedgerBatchParam riskSourceLedgerBatchParam = new RiskSourceLedgerBatchParam();
            riskSourceLedgerBatchParam.setIdList(targetIds);
            List<RiskSourceLedgerVO> riskSourceList = riskSourceLedgerClient.queryRiskSourceLedgerBatchPost(riskSourceLedgerBatchParam);
            objects = riskSourceList.stream().map(riskSource -> {
                InspectTargetInfoDTO vo = setRiskSourceInfo(riskSource);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        } else if (InspectTargetCategoryEnum.SMALL_ENGINEERING.name().equals(category)) {
            ProjectScheduleQueryParam param = new ProjectScheduleQueryParam();
            param.setProjectIds(targetIds);
            List<ProjectScheduleVO> projectScheduleVOS = projectScheduleClient.projectScheduleBatchPost(param);
            if (CollectionUtils.isEmpty(projectScheduleVOS)) {
                return new ArrayList<>();
            }
            objects = projectScheduleVOS.stream().map(projectScheduleVO -> {
                InspectTargetInfoDTO vo = setSmallEngineerInfo(projectScheduleVO);
                if (targetIdStandardIdsMap != null) {
                    this.setStandardIds(vo, targetIdStandardIdsMap);
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return objects;
    }

    /**
     * 设置对象关联的标准
     *
     * @param vo                     对象信息
     * @param targetIdStandardIdsMap 关联标准id
     */
    private void setStandardIds(InspectTargetInfoDTO vo, Map<Long, String> targetIdStandardIdsMap) {
        List<Long> ids = Collections.emptyList();
        if (targetIdStandardIdsMap != null) {
            String standardIds = targetIdStandardIdsMap.getOrDefault(vo.getTargetId(), null);
            if (StringUtils.isNotEmpty(standardIds)) {
                ids = Arrays.stream(standardIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
            }
        } else {
            ids = standardTypeReferQueryService.getStandardIdsByTarget(vo.getTargetCategory(), vo.getTargetType());
        }
        vo.setStandardIds(ids);
    }

    /**
     * @Author wql
     * @Description 查询对象信多个
     * @Date 15:55 2023/6/6
     **/
    public List<InspectTargetInfoDTO> getTargetInfo(List<Long> targetIdList, String targetType) {
        List<InspectTargetInfoDTO> objects = new ArrayList<>();
        try {
            if (InspectTargetCategoryEnum.DEVICE.name().equals(targetType)) {
                DevicePostByIdSetParam param = new DevicePostByIdSetParam();
                param.setDeviceIdSet(new HashSet<>(targetIdList));
                List<DeviceBaseDetailsDTO> detailsListByDeviceId = deviceCenterStdClient.postDetailsListByDeviceId(param);
                detailsListByDeviceId.forEach(device -> objects.add(setDeviceInfo(device)));
            } else if (InspectTargetCategoryEnum.STALL.name().equals(targetType)) {
                StallPostQueryParam param = new StallPostQueryParam();
                param.setStallIdList(targetIdList);
                List<StallDTO> stalls = stallClient.postStallListByIds(param);
                for (StallDTO stall : stalls) {
                    objects.add(setStallInfo(stall));
                }
            } else if (InspectTargetCategoryEnum.PROPERTY.name().equals(targetType)) {
                PropertyDetailedByIdListParam param = new PropertyDetailedByIdListParam();
                param.setIdList(targetIdList);
                List<PropertyDetailedVO> propertyList = propertyDetailedClient.queryPropertyDetailBatchPost(param);
                for (PropertyDetailedVO propertyDetailedVO : propertyList) {
                    objects.add(setPropertyInfo(propertyDetailedVO));
                }
            } else if (InspectTargetCategoryEnum.SITE_POINT.name().equals(targetType)) {
                List<InspectScenePointPO> poList = inspectScenePointService.listByIds(targetIdList);
                for (InspectScenePointPO point : poList) {
                    objects.add(setFieldPointInfo(point));
                }
            } else if (InspectTargetCategoryEnum.RISK_SOURCE.name().equals(targetType)) {
                RiskSourceLedgerBatchParam param = new RiskSourceLedgerBatchParam();
                param.setIdList(targetIdList);
                List<RiskSourceLedgerVO> riskSourceList = riskSourceLedgerClient.queryRiskSourceLedgerBatchPost(param);
                for (RiskSourceLedgerVO riskSourceLedgerVO : riskSourceList) {
                    objects.add(setRiskSourceInfo(riskSourceLedgerVO));
                }
            } else if (InspectTargetCategoryEnum.SMALL_ENGINEERING.name().equals(targetType)) {
                ProjectScheduleQueryParam param = new ProjectScheduleQueryParam();
                param.setProjectIds(targetIdList);
                List<ProjectScheduleVO> projectScheduleVOS = projectScheduleClient.projectScheduleBatchPost(param);
                if (CollectionUtils.isEmpty(projectScheduleVOS)) {
                    return new ArrayList<>();
                }
                projectScheduleVOS.forEach(projectScheduleVO -> objects.add(setSmallEngineerInfo(projectScheduleVO)));
            }
        } catch (Exception e) {
            log.error("获取对象信息异常:", e);
        }
        return objects;
    }

    /**
     * 获取单个对象的详情
     *
     * @param targetId       对象id
     * @param targetCategory 对象类别
     *
     * @return 对象信息
     */
    public InspectTargetInfoDTO getTargetInfoById(Long targetId, String targetCategory) {
        try {
            InspectTargetCategoryEnum categoryEnum = InspectTargetCategoryEnum.valueOf(targetCategory);
            switch (categoryEnum) {
                case DEVICE:
                    List<DeviceBaseDetailsDTO> detailsListByDeviceId = deviceCenterStdClient.getDetailsListByDeviceId(Collections.singleton(targetId));
                    return setDeviceInfo(CollectionUtils.isNotEmpty(detailsListByDeviceId) ? detailsListByDeviceId.get(0) : null);
                case STALL:
                    return setStallInfo(stallClient.getStallById(targetId));
                case PROPERTY:
                    return setPropertyInfo(propertyDetailedClient.queryById(targetId));
                case SITE_POINT:
                    return setFieldPointInfo(inspectScenePointService.getById(targetId));
                case RISK_SOURCE:
                    return setRiskSourceInfo(riskSourceLedgerClient.queryById(targetId));
                case SMALL_ENGINEERING:
                    return setSmallEngineerInfo(projectScheduleClient.projectScheduleGetById(targetId));
                default:
                    return new InspectTargetInfoDTO();
            }
        } catch (IllegalArgumentException e) {
            log.error("查询单个对象详情信息异常:", e);
        }
        return new InspectTargetInfoDTO();
    }

    /**
     * 设置小散工程目标对象
     *
     * @param projectScheduleVO
     *
     * @return
     */
    private InspectTargetInfoDTO setSmallEngineerInfo(ProjectScheduleVO projectScheduleVO) {
        InspectTargetInfoDTO vo = new InspectTargetInfoDTO();
        if (ObjectUtils.isNotEmpty(projectScheduleVO)) {
            vo.setTargetCategory(InspectTargetCategoryEnum.SMALL_ENGINEERING.name());
            vo.setTargetId(projectScheduleVO.getId());
            vo.setTargetCode(projectScheduleVO.getProjectNum());
            vo.setTargetName(projectScheduleVO.getProjectName());
            vo.setTargetType(String.valueOf(projectScheduleVO.getProjectType()));
            vo.setTargetTypeName(projectScheduleVO.getProjectTypeName());
            vo.setAddress(projectScheduleVO.getAddress());
        }
        return vo;
    }

    /**
     * @Author wql
     * @Description 拼接地区id
     * @Date 15:25 2023/5/10
     **/
    public String jointAreaId(List<AreaDTO> areas) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(areas).orElse(Collections.emptyList()).forEach(area -> {
            sb.append(area.getId()).append("-");
        });
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    /**
     * @Author wql
     * @Description 拼接区域id
     * @Date 15:56 2023/6/6
     **/
    public String joinAreaId(LinkedList<Long> ids) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(ids).orElse(new LinkedList<>()).forEach(id -> {
            sb.append(id).append("-");
        });
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    /**
     * @Author wql
     * @Description 拼接资产对象地址名称
     * @Date 15:20 2023/5/10
     **/
    public String jointName(List<AreaDTO> areas) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(areas).orElse(Collections.emptyList()).forEach(area -> {
            sb.append(area.getName()).append("-");
        });
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }
}
