package com.guhecloud.cube.srv.gen.tag.tag.enums;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.client.param.StallQueryParam;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolLocationClient;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.tag.tag.dto.response.BaseTargetInfoResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum TagRefTypeEnum {
    /**
     * AREA
     */
    AREA("区域") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new AreaQueryParam();
            query.setIds(idList);
            AreaClient areaClient = ApplicationContextHolder.getBean(AreaClient.class);
            var areas = areaClient.getAreaIdTypes(query);
            if (CollectionUtils.isEmpty(areas)) {
                return new ArrayList<>();
            }
            return areas.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTargetType(AREA.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    /**
     * 档位
     */
    STALL("档位") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new StallQueryParam();
            query.setIds(idList);
            StallClient stallClient = ApplicationContextHolder.getBean(StallClient.class);
            List<StallIdTypeDTO> stallIdTypes = stallClient.getStallIdTypes(query);
            if (CollectionUtils.isEmpty(stallIdTypes)) {
                return new ArrayList<>();
            }
            return stallIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTypeId(t.getTypeId());
                response.setTargetType(STALL.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },

    /**
     * 设备
     */
    DEVICE("设备") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new DeviceFilterPageParam();
            query.setMatchedDeviceIds(new ArrayList<>(idList));
            var deviceClient = ApplicationContextHolder.getBean(DeviceCenterStdClient.class);
            List<DeviceIdTypeDTO> deviceIdTypeDTOS = deviceClient.deviceInfoIdTypes(query);
            if (CollectionUtils.isEmpty(deviceIdTypeDTOS)) {
                return new ArrayList<>();
            }
            return deviceIdTypeDTOS.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTargetType(DEVICE.name());
                response.setTypeId(t.getTypeId());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    /**
     * 资产
     */
    PROPERTY("资产") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new PropertyDetailedPageParam();
            query.setIds(idList);
            var propertyDetailedClient = ApplicationContextHolder.getBean(PropertyDetailedClient.class);
            List<PropertyDetailIdTypeDTO> propertyDetailedIdTypes = propertyDetailedClient.getPropertyDetailedIdTypes(query);
            if (CollectionUtils.isEmpty(propertyDetailedIdTypes)) {
                return new ArrayList<>();
            }
            return propertyDetailedIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTypeId(Objects.nonNull(t.getTypeId()) ? t.getTypeId().toString() : null);
                response.setTargetType(PROPERTY.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    /**
     * 现场点
     */
    SITE_POINT("现场点") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new InspectScenePointQueryParam();
            query.setIds(idList);
            var scenePointClient = ApplicationContextHolder.getBean(ScenePointClient.class);
            List<ScenePointIdTypeDTO> scenePointIdTypes = scenePointClient.getScenePointIdTypes(query);
            if (CollectionUtils.isEmpty(scenePointIdTypes)) {
                return new ArrayList<>();
            }
            return scenePointIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTypeId(t.getTypeId());
                response.setTargetType(SITE_POINT.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    /**
     * 风险源
     */
    RISK_SOURCE("风险源") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new RiskSourceLedgerPageParam();
            query.setIds(idList);
            var riskSourceLedgerClient = ApplicationContextHolder.getBean(RiskSourceLedgerClient.class);
            List<RiskSourceIdTypeVO> riskSourceIdTypes = riskSourceLedgerClient.getRiskSourceIdTypes(query);
            if (CollectionUtils.isEmpty(riskSourceIdTypes)) {
                return new ArrayList<>();
            }
            return riskSourceIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTypeId(t.getTypeId());
                response.setTargetType(RISK_SOURCE.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());

        }
    },

    /**
     * 小散工程
     */
    SMALL_ENGINEERING("小散工程") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new ProjectScheduleQuery();
            query.setIdList(new ArrayList<>(idList));
            var projectScheduleClient = ApplicationContextHolder.getBean(ProjectScheduleClient.class);
            List<ProjectScheduleIdTypeDTO> projectScheduleIdTypes = projectScheduleClient.getProjectScheduleIdTypes(query);
            if (CollectionUtils.isEmpty(projectScheduleIdTypes)) {
                return new ArrayList<>();
            }
            return projectScheduleIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTypeId(Objects.nonNull(t.getTypeId()) ? t.getTypeId().toString() : null);
                response.setTargetType(SMALL_ENGINEERING.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    /**
     * 巡更点
     */
    PATROL_POINT("巡更点") {
        @Override
        public List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                          Map<Long, Set<Long>> refIdMap
                                                         ) {
            var query = new PatrolLocationPageParam();
            query.setIdSet(idList);
            var patrolLocationClient = ApplicationContextHolder.getBean(PatrolLocationClient.class);
            List<PatrolLocationIdTypeDTO> patrolPointIdTypes = patrolLocationClient.getPatrolPointIdTypes(query);
            if (CollectionUtils.isEmpty(patrolPointIdTypes)) {
                return new ArrayList<>();
            }
            return patrolPointIdTypes.stream().map(t -> {
                BaseTargetInfoResponse response = new BaseTargetInfoResponse();
                response.setTargetId(t.getId());
                response.setTargetName(t.getName());
                response.setTargetType(PATROL_POINT.name());
                setTagInfo(tagBusinessMap, refIdMap, t.getId(), response);
                return response;
            }).collect(Collectors.toList());
        }
    },
    ;

    /**
     * 描述信息
     */
    private final String description;

    public abstract List<BaseTargetInfoResponse> getTargetInfo(Set<Long> idList, Map<Long, String> tagBusinessMap,
                                                               Map<Long, Set<Long>> refIdMap
                                                              );

    private static void setTagInfo(Map<Long, String> tagBusinessMap, Map<Long, Set<Long>> refIdMap, Long id,
                                   BaseTargetInfoResponse response
                                  ) {
        Set<Long> tagIds = refIdMap.get(id);

        if (CollectionUtils.isNotEmpty(tagIds)) {
            response.setTagIds(StringUtils.join(",", tagIds));
            String tagNames = tagIds.stream().filter(tagBusinessMap::containsKey)
                    .map(tagBusinessMap::get)
                    .collect(Collectors.joining(","));
            response.setTagNames(tagNames);
        }
    }
}
