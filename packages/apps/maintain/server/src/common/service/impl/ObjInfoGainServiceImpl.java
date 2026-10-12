package com.guhecloud.cube.srv.gen.maintain.server.common.service.impl;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.ObjInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.ObjInfoGainService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 对象信息获取
 * @date 2023/11/8 15:02
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ObjInfoGainServiceImpl implements ObjInfoGainService {
    private final StallClient stallClient;
    private final PropertyDetailedClient propertyDetailedClient;
    private final InspectScenePointService inspectScenePointService;
    private final DeviceCenterStdClient deviceInstanceClient;
    private final AreaClient areaClient;
    private final PatrolLocationService patrolLocationService;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final ProjectScheduleClient projectScheduleClient;

    @Override
    public ObjInfoDTO getObjInfo(long objId, String objType) {
        final ObjInfoDTO objInfoDTO = new ObjInfoDTO();
        objInfoDTO.setId(objId);
        if (ObjTypeEnum.DEVICE.name().equals(objType)) {
            final DeviceDetailDTO deviceById = deviceInstanceClient.getDeviceById(objId);
            if (ObjectUtils.isNotEmpty(deviceById) && ObjectUtils.isNotEmpty(deviceById.getBaseArchivesInfo())) {
                DeviceBaseArchivesInfoDTO detail = deviceById.getBaseArchivesInfo();
                objInfoDTO.setObjName(detail.getName());
                Pair<Long, String> area = detail.getArea();
                if (ObjectUtils.isNotEmpty(area) && ObjectUtils.isNotNull(area.getKey())) {
                    Long areaId = area.getKey();
                    objInfoDTO.setAreaId(areaId.toString());
                    List<Long> parentIds = areaClient.getParentIds(areaId);
                    List<Long> areaIdList = new ArrayList<>();
                    if (ObjectUtils.isNotEmpty(parentIds)) {
                        areaIdList.addAll(parentIds);
                    }
                    areaIdList.add(areaId);
                    List<AreaDTO> areaDTOList = areaClient.getByIds(areaIdList);
                    List<String> collect = areaDTOList.stream().map(AreaDTO::getName).collect(Collectors.toList());
                    objInfoDTO.setAreaName(String.join("-", collect));
                }
            }
        } else if (ObjTypeEnum.STALL.name().equals(objType)) {
            final StallDTO byId = stallClient.getStallById(objId);
            if (ObjectUtils.isNotNull(byId)) {
                objInfoDTO.setObjName(byId.getName());
                objInfoDTO.setAreaId(byId.getArea());
                objInfoDTO.setAreaName(byId.getAreaName());
            }
        } else if (ObjTypeEnum.SITE_POINT.name().equals(objType)) {
            final ScenePointVO byId = inspectScenePointService.getScenePointDetail(objId);
            if (ObjectUtils.isNotNull(byId)) {
                objInfoDTO.setObjName(byId.getName());
                objInfoDTO.setAreaId(byId.getAreaId());
                objInfoDTO.setAreaName(byId.getAreaName());
            }
        } else if (ObjTypeEnum.PROPERTY.name().equals(objType)) {
            final PropertyDetailedVO propertyVO = propertyDetailedClient.queryById(objId);
            if (ObjectUtils.isNotNull(propertyVO)) {
                objInfoDTO.setObjName(propertyVO.getName());
                List<AreaDTO> area = propertyVO.getArea();
                List<String> areaIds = new ArrayList<>();
                List<String> areaNames = new ArrayList<>();
                if (ObjectUtils.isNotEmpty(area)) {
                    area.forEach(a -> {
                        areaIds.add(a.getId().toString());
                        areaNames.add(a.getName());
                    });
                }
                objInfoDTO.setAreaId(String.join("-", areaIds));
                objInfoDTO.setAreaName(String.join("-", areaNames));
            }
        } else if (ObjTypeEnum.PATROL_POINT.name().equals(objType)) {
            PatrolLocationVO patrolLocationVO = patrolLocationService.queryById(objId);
            if (ObjectUtils.isNotNull(patrolLocationVO)) {
                objInfoDTO.setObjName(patrolLocationVO.getPatrolLocationName());
                objInfoDTO.setAreaId(patrolLocationVO.getAreaId());
                objInfoDTO.setAreaName(
                        !CollectionUtils.isEmpty(patrolLocationVO.getArea()) ? patrolLocationVO.getArea().getFirst().getName() : null);
            }
        } else if (ObjTypeEnum.RISK_SOURCE.name().equals(objType)) {
            RiskSourceLedgerVO riskSourceLedgerVO = riskSourceLedgerClient.queryById(objId);
            if (ObjectUtils.isNotNull(riskSourceLedgerVO)) {
                objInfoDTO.setObjName(riskSourceLedgerVO.getName());
                objInfoDTO.setAreaId(riskSourceLedgerVO.getAreaId());
                objInfoDTO.setAreaName(riskSourceLedgerVO.getAreaName());
            }
        } else if (ObjTypeEnum.SMALL_ENGINEERING.name().equals(objType)) {
            ProjectScheduleVO projectScheduleVO = projectScheduleClient.projectScheduleGetById(objId);
            if (ObjectUtils.isNotNull(projectScheduleVO)) {
                objInfoDTO.setObjName(projectScheduleVO.getProjectName());
            }
        }
        log.info("对象信息:{}", JSON.toJSONString(objInfoDTO));
        return objInfoDTO;
    }
}
