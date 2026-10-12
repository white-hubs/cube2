package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectionDataMigrationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectResultCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardTypeReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl.InspectPlanCommandServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectScenePointMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectSceneTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TjAreaDataMigrationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.CnpAreaPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectSceneTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.BasicObjectTypeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.BasicScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectStandardTypeReferVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectStandardVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.FireinspPointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionAreaVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ShDictDataVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-26  09:56
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionDataMigrationServiceImpl implements InspectionDataMigrationService {
    private final InspectSceneTypeMapper inspectSceneTypeMapper;
    private final InspectScenePointMapper inspectScenePointMapper;
    private final InspectScenePointService inspectScenePointService;
    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final InspectStandardMapper inspectStandardMapper;
    private final InspectStandardCommandService inspectStandardCommandService;
    private final InspectStandardItemCommandService inspectStandardItemCommandService;
    private final InspectStandardItemMapper inspectStandardItemMapper;
    private final InspectStandardTypeReferCommandService inspectStandardTypeReferCommandService;
    private final InspectResultMapper inspectResultMapper;
    private final InspectResultCommandService inspectResultCommandService;
    private final InspectPlanCommandServiceImpl inspectPlanCommandService;
    private final InspectPlanMapper inspectPlanMapper;
    private final InspectRecordMapper inspectRecordMapper;
    private final InspectRecordCommandService inspectRecordCommandService;
    private final TjAreaDataMigrationMapper cnpAreaDataMigrationMapper;
    private final InspectRecordItemCommandService inspectRecordItemCommandService;

    private Map<String, InspectSceneTypePO> objType = new HashMap<>();
    private static Map<String, InspectSceneTypePO> shDictDataType = new HashMap<>();

    @Override
    public Boolean inspectionDataMigration() {
        List<AreaDTO> areaDTOS = this.areaClient.listAreas();
        List<OrganizationDTO> organizations = this.organizationClient.getOrganizations(new OrganizationQueryParam());
        List<BasicObjectTypeVO> objectType = inspectSceneTypeMapper.getAllCnpObjectType();
        objectType.forEach(ex -> {
            InspectSceneTypePO type = InspectSceneTypePO.builder()
                    .id(IdUtils.next())
                    .name(ex.getObjectTypeName())
                    .build();
            inspectSceneTypeMapper.insert(type);
            inspectStandardMapper.updateCnpInspectStandard(ex.getId(), type.getId());
            objType.put(ex.getId(), type);
        });
        List<BasicScenePointVO> data = inspectScenePointMapper.getCnpScenePoint();
        List<InspectScenePointPO> scenePointPOList = new ArrayList<>();
        data.forEach(ex -> {
            try {
                scenePointPOList.add(this.getInspectScenePointPO(ex, areaDTOS, organizations));
            } catch (Exception e) {
                log.error("迁移", e);
            }
        });
        return inspectScenePointService.saveBatch(scenePointPOList);
    }

    @Override
    public Boolean inspectionItemDataMigration() {
        List<CnpInspectStandardVO> inspectStandardList = inspectStandardMapper.getCnpInspectStandard();
        List<InspectStandardPO> list = new ArrayList<>();
        List<InspectStandardTypeReferPO> typeReferList = new ArrayList<>();
        List<InspectStandardItemPO> itemList = new ArrayList<>();
        inspectStandardList.stream().forEach(ex -> {
            InspectStandardPO inspectStandardPO = new InspectStandardPO();
            inspectStandardPO.setId(ex.getId());
            inspectStandardPO.setCode(ex.getStandardCode());
            inspectStandardPO.setName(ex.getStandardName());
            list.add(inspectStandardPO);
        });
        inspectStandardCommandService.saveBatch(list);
        List<CnpInspectStandardTypeReferVO> typeReferVOList = inspectStandardMapper.selectCnpInspectStandard();
        typeReferVOList.forEach(ex -> {
            InspectStandardTypeReferPO typeReferPO = new InspectStandardTypeReferPO();
            typeReferPO.setStandardId(ex.getInspectStandardId());
            typeReferPO.setTargetCategory(this.getInspectTargetCategoryEnum(ex.getReferTypeCode()));
            typeReferPO.setTargetType(ex.getReferTypeId());
            typeReferPO.setDefaultStandard(ex.getDefaultStandard());
            typeReferPO.setDeleted(false);
            typeReferList.add(typeReferPO);
        });
        inspectStandardTypeReferCommandService.saveBatch(typeReferList);
        List<CnpInspectItemVO> cnpItemList = inspectStandardItemMapper.getCnpInspectItem();
        for (CnpInspectItemVO ex : cnpItemList) {
            InspectStandardItemPO itemPO = new InspectStandardItemPO();
            itemPO.setId(ex.getId());
            itemPO.setName(ex.getInspectItemName());
            itemPO.setStandardId(ex.getInspectStandardId());
            itemPO.setResultId(ex.getInspectResultId());
            //            itemPO.setResultItemValue(ex.getInspectResultId());
            itemList.add(itemPO);
            if (itemList.size() > 100) {
                inspectStandardItemCommandService.saveBatch(itemList);
                itemList = new ArrayList<>();
            }
        }
        inspectStandardItemCommandService.saveBatch(itemList);
        List<InspectResultPO> resultPOList = new ArrayList<>();
        List<CnpInspectResultVO> cnpInspectResultList = inspectResultMapper.getCnpInspectResult();
        cnpInspectResultList.stream().forEach(ex -> {
            InspectResultPO inspectResultPO = new InspectResultPO();
            inspectResultPO.setId(ex.getId());
            inspectResultPO.setName(ex.getResultName());
            inspectResultPO.setItemName(ex.getItemName());
            inspectResultPO.setItemValue(ex.getItemValue());
            inspectResultPO.setEnable(true);
            inspectResultPO.setErrorFlag(ex.getErrorFlag());
            inspectResultPO.setCode(ex.getId().toString());
            resultPOList.add(inspectResultPO);
        });
        inspectResultCommandService.saveBatch(resultPOList);

        return true;
    }

    @Override
    public Boolean shInspectionDataMigration() {
        List<AreaDTO> areaDTOS = this.areaClient.listAreas();
        List<ShDictDataVO> dicData = inspectScenePointMapper.getSnDictData();
        dicData.forEach(ex -> {
            InspectSceneTypePO typePO = shDictDataType.get(ex.getCode());
            if (ObjectUtils.isEmpty(shDictDataType.get(ex.getCode()))) {
                typePO = InspectSceneTypePO.builder()
                        .id(IdUtils.next())
                        .name(ex.getName())
                        .build();
                shDictDataType.put(ex.getCode(), typePO);
            }
            inspectSceneTypeMapper.insert(typePO);

        });
        List<FireinspPointVO> data = inspectScenePointMapper.getSnScenePoint();
        List<InspectScenePointPO> scenePointPOList = new ArrayList<>();
        data.forEach(ex -> {
            try {
                scenePointPOList.add(this.getshInspectScenePointPO(ex, areaDTOS));
            } catch (Exception e) {
                log.error("迁移", e);
            }
        });
        return inspectScenePointService.saveBatch(scenePointPOList);
    }

    @Override
    public void tjInspectionDataMigration() {
        //        //巡检标准
        //        List<FireinspStandard> tjFireinspStandard = inspectStandardItemMapper.getTjFireinspStandard();
        //        List<FireinspItem> tjFireinspItem = inspectStandardItemMapper.getTjFireinspItem();
        //        Map<String, List<FireinspItem>> tjFireinspMap = new HashMap<>();
        //        if (CollectionUtils.isNotEmpty(tjFireinspItem)) {
        //            tjFireinspMap
        //                    = tjFireinspItem.stream().collect(Collectors.groupingBy(FireinspItem::getStandardId));
        //        }
        //        List<FireinspStandardTarget> tjFireinspStandardTarget = inspectStandardItemMapper.getTjFireinspStandardTarget();
        //        List<InspectStandardPO> list = new ArrayList<>();
        //        List<InspectStandardItemPO> itemPOS = new ArrayList<>();
        //        List<InspectStandardTypeReferPO> typeReferPOS = new ArrayList<>();
        //        for (FireinspStandard ex : tjFireinspStandard) {
        //            InspectStandardPO inspectStandardPO = new InspectStandardPO();
        //            inspectStandardPO.setId(ex.getId());
        //            inspectStandardPO.setCode(ex.getCode());
        //            inspectStandardPO.setName(ex.getName());
        //            switch (ex.getType()) {
        //                case "fireinspStandardType_device":
        //                    InspectStandardTypeReferPO typeReferPO = new InspectStandardTypeReferPO();
        //                    typeReferPO.setId(IdWorker.getId());
        //                    typeReferPO.setStandardId(ex.getId());
        //                    typeReferPO.setTargetCategory("DEVICE");
        //                    typeReferPOS.add(typeReferPO);
        //                    break;
        //                case "fireinspStandardType_scene":
        //                    InspectStandardTypeReferPO typeReferPO1 = new InspectStandardTypeReferPO();
        //                    typeReferPO1.setId(IdWorker.getId());
        //                    typeReferPO1.setStandardId(ex.getId());
        //                    typeReferPO1.setTargetCategory("DEVICE");
        //                    typeReferPOS.add(typeReferPO1);
        //                    break;
        //                case "fireinspStandardType_store":
        //                    InspectStandardTypeReferPO typeReferPO2 = new InspectStandardTypeReferPO();
        //                    typeReferPO2.setId(IdWorker.getId());
        //                    typeReferPO2.setStandardId(ex.getId());
        //                    typeReferPO2.setTargetCategory("DEVICE");
        //                    typeReferPOS.add(typeReferPO2);
        //                    break;
        //            }
        //            list.add(inspectStandardPO);
        //        }

        //巡检项
    }

    private static final String CYCLETYPE_ONCE = "fireinspCycleType_once";
    private static final String CYCLETYPE_EVERDAY = "fireinspCycleType_everyday";
    private static final String CYCLETYPE_EVERWEEK = "fireinspCycleType_everyweek";
    private static final String CYCLETYPE_EVERMONTH = "fireinspCycleType_everymonth";

    @Override
    public void tjInspectionPlanDataMigration() {
        List<InspectPlanPO> planPOList = new ArrayList<>();
        List<FireinspPlanPO> tjFireinspPlan = inspectPlanMapper.getFireinspPlan();
        tjFireinspPlan.forEach(ex -> {
            InspectPlanPO planPO = new InspectPlanPO();
            planPO.setId(Long.parseLong(ex.getId()));
            planPO.setCode(ex.getCode());
            planPO.setName(ex.getName());
            planPO.setBeginTime(ex.getBeginTime());
            planPO.setEndTime(ex.getEndTime());
            planPO.setValidBeginTime(ex.getBeginTime());
            planPO.setValidEndTime(ex.getEndTime());
            planPO.setEnable(ex.getEnabled());
            planPO.setStatus(DateUtils.compare(planPO.getEndTime(), LocalDateTime.now()) <= 0 ? InspectPlanExecStatusEnum.EXECUTING
                                                                                              : InspectPlanExecStatusEnum.OVER);
            planPO.setCycle(ex.getCycleType().equals(CYCLETYPE_ONCE));
            planPO.setLevel(InspectLevelEnum.GENERAL);
            planPO.setReceiveType("1");
            planPOList.add(planPO);
        });
        inspectPlanCommandService.saveBatch(planPOList);
    }

    @Override
    public void tjInspectionTaskDataMigration() {

    }

    @Override
    public void tjInspectionRecordDataMigration() {
        List<InspectRecordPO> resultPOList = new ArrayList<>();
        List<OrganizationDTO> dataDep = cnpAreaDataMigrationMapper.getAllDep();
        List<FireinspRecordPO> cnpInspectResultList = inspectRecordMapper.getTjInspectResult();
        List<UserDTO> userDTOS = cnpAreaDataMigrationMapper.getAllUser();
        Map<String, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getMobile, item -> item));
        try {
            for (FireinspRecordPO ex : cnpInspectResultList) {
                InspectRecordPO inspectResultPO = new InspectRecordPO();
                inspectResultPO.setId(Long.parseLong(ex.getId()));
                if (ObjectUtils.isNotEmpty(ex.getTaskId())) {
                    inspectResultPO.setTaskId(Long.parseLong(ex.getTaskId()));
                }
                inspectResultPO.setTaskName(ex.getTaskName());
                if (ObjectUtils.isNotEmpty(ex.getPlanId())) {
                    inspectResultPO.setPlanId(Long.parseLong(ex.getPlanId()));
                }
                inspectResultPO.setPlanName(ex.getPlanName());
                if (ObjectUtils.isNotEmpty(ex.getTargetId())) {
                    inspectResultPO.setTargetId(Long.parseLong(ex.getTargetId()));
                    inspectResultPO.setTargetName(ex.getTargetName());
                    inspectResultPO.setTargetCategory(InspectTargetCategoryEnum.DEVICE.name());
                } else if (ObjectUtils.isNotEmpty(ex.getPointId())) {
                    inspectResultPO.setTargetId(Long.parseLong(ex.getPointId()));
                    inspectResultPO.setTargetName(ex.getPointName());
                    inspectResultPO.setTargetCategory(InspectTargetCategoryEnum.SITE_POINT.name());
                }
                inspectResultPO.setStatus(InspectRecordStatusEnum.OVER);
                inspectResultPO.setTime(ex.getTime());
                inspectResultPO.setResult("fireinspRecordResult_normal".equals(ex.getResult()) ? InspectRecordResultEnum.NORMAL
                                                                                               : InspectRecordResultEnum.ABNORMAL);
                OrganizationDTO maintenanceDep = this.getOrganization(ex.getDeptName(), dataDep);
                inspectResultPO.setOrganizationId(maintenanceDep.getId());
                inspectResultPO.setOrganizationName(maintenanceDep.getName());
                UserDTO userDTO = userDTOMap.get(ex.getUsername());
                if (ObjectUtils.isNotEmpty(userDTO)) {
                    inspectResultPO.setUserId(userDTO.getId());
                    inspectResultPO.setUsername(userDTO.getUsername());
                    inspectResultPO.setNickname(userDTO.getName());
                }
                // 所属区域
                String areaId = null;
                if (ObjectUtils.isNotEmpty(ex.getAreaId1())) {
                    areaId = ex.getAreaId1();
                }
                if (ObjectUtils.isNotEmpty(ex.getAreaId2())) {
                    if (ObjectUtils.isNotEmpty(areaId)) {
                        areaId = areaId + ",";
                    }
                    areaId = areaId + ex.getAreaId2();
                }
                if (ObjectUtils.isNotEmpty(ex.getAreaId3())) {
                    if (ObjectUtils.isNotEmpty(areaId)) {
                        areaId = areaId + ",";
                    }
                    areaId = areaId + ex.getAreaId3();
                }
                Map<String, String> areaMap = this.getArea(areaId);
                inspectResultPO.setAreaId(areaMap.get("id"));
                inspectResultPO.setAreaName(areaMap.get("name"));
                resultPOList.add(inspectResultPO);
                if (resultPOList.size() > 1000) {
                    inspectRecordCommandService.saveBatch(resultPOList);
                    resultPOList = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            log.error("巡检记录数据迁移失败", e);
        }
        inspectRecordCommandService.saveBatch(resultPOList);
    }

    @Override
    public void tjInspectionRecordDataItemMigration() {
        List<InspectRecordItemPO> list = new ArrayList<>();
        List<FireinspRecordItemPO> items = cnpAreaDataMigrationMapper.getAllRecordItem();
        for (FireinspRecordItemPO ex : items) {
            InspectRecordItemPO item = new InspectRecordItemPO();
            item.setId(Long.parseLong(ex.getId()));
            item.setRecordId(Long.parseLong(ex.getRecordId()));
            item.setTempAddFlag(false);
            if (ObjectUtils.isNotEmpty(ex.getStandardId())) {
                item.setStandardId(Long.parseLong(ex.getStandardId()));
                item.setStandardName(ex.getStandardName());
            }
            item.setItemName(ex.getName());
            list.add(item);
            if (list.size() > 1000) {
                inspectRecordItemCommandService.saveBatch(list);
                list = new ArrayList<>();
            }
        }
        inspectRecordItemCommandService.saveBatch(list);
    }

    private InspectScenePointPO getInspectScenePointPO(BasicScenePointVO scenePoint, List<AreaDTO> areaDTOS,
                                                       List<OrganizationDTO> organizationList
                                                      ) {
        InspectScenePointPO scenePointPO = new InspectScenePointPO();
        scenePointPO.setId(scenePoint.getId());
        scenePointPO.setCodeNum(scenePoint.getSceneCode());
        scenePointPO.setName(scenePoint.getSceneName());
        InspectSceneTypePO sceneType = objType.get(scenePoint.getSceneTypeId());
        if (ObjectUtils.isNotEmpty(sceneType)) {
            scenePointPO.setTypeId(sceneType.getId());
            scenePointPO.setTypeName(sceneType.getName());
        }
        OrganizationDTO organization = this.getOrganization(scenePoint.getManageDeptId(), organizationList);
        if (ObjectUtils.isNotEmpty(organization)) {
            scenePointPO.setDepId(organization.getId());
            scenePointPO.setDepName(organization.getName());
        }
        if (ObjectUtils.isNotEmpty(scenePoint.getAreaId())) {
            //            AreaDTO area = this.getArea(scenePoint.getAreaId());
            //            if (ObjectUtils.isNotEmpty(area) && ObjectUtils.isNotEmpty(area.getId())) {
            //                scenePointPO.setAreaId(area.getId().toString());
            //            }
        }
        scenePointPO.setDetailAddress(scenePoint.getAddrDetail());
        //        scenePointPO.setLocation();
        return scenePointPO;
    }

    private InspectScenePointPO getshInspectScenePointPO(FireinspPointVO scenePoint, List<AreaDTO> areaDTOS) {
        InspectScenePointPO scenePointPO = new InspectScenePointPO();
        scenePointPO.setId(Long.parseLong(scenePoint.getId()));
        scenePointPO.setCodeNum(scenePoint.getCode());
        scenePointPO.setName(scenePoint.getName());
        // 现场点类型
        InspectSceneTypePO sceneType = shDictDataType.get(scenePoint.getType());
        if (ObjectUtils.isNotEmpty(sceneType)) {
            scenePointPO.setTypeId(sceneType.getId());
            scenePointPO.setTypeName(sceneType.getName());
        }
        OrganizationDTO organization = organizationClient.getOrganizationsByName(scenePoint.getDeptName());
        if (ObjectUtils.isNotEmpty(organization)) {
            scenePointPO.setDepId(organization.getId());
            scenePointPO.setDepName(organization.getName());
        }
        // 现场点区域
        if (ObjectUtils.isNotEmpty(scenePoint.getAreaId3())) {
            scenePointPO.setAreaId(this.getShArea(scenePoint.getAreaId3(), areaDTOS));
        } else if (ObjectUtils.isNotEmpty(scenePoint.getAreaId2())) {
            scenePointPO.setAreaId(this.getShArea(scenePoint.getAreaId2(), areaDTOS));
        } else if (ObjectUtils.isNotEmpty(scenePoint.getAreaId1())) {
            scenePointPO.setAreaId(this.getShArea(scenePoint.getAreaId1(), areaDTOS));
        }
        scenePointPO.setDetailAddress(scenePoint.getAddress());
        return scenePointPO;
    }

    private Map<String, String> getArea(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "");
        map.put("name", "");
        if (ObjectUtils.isEmpty(id)) {
            return map;
        }
        String areaId = "";
        String areaName = "";
        String[] split = id.split(",");
        CnpAreaPO cnpArea = this.cnpAreaDataMigrationMapper.getAreaById(split[0]);
        if (ObjectUtils.isNotEmpty(cnpArea)) {
            areaId = cnpArea.getId().toString();
            areaName = cnpArea.getName();
            if (split.length > 1) {
                CnpAreaPO cnpArea2 = this.cnpAreaDataMigrationMapper.selectById(Long.parseLong(split[1]));
                CnpAreaPO area = this.cnpAreaDataMigrationMapper.getAreaByParentId(cnpArea2.getName(), cnpArea.getId());
                if (ObjectUtils.isNotEmpty(area)) {
                    areaId = areaId + "," + area.getId().toString();
                    areaName = areaName + "," + area.getName();
                }
            }
        }
        map.put("id", areaId);
        map.put("name", areaName);
        return map;
    }

    private OrganizationDTO getOrganization(String depName, List<OrganizationDTO> organizations) {
        if (ObjectUtils.isEmpty(depName)) {
            return new OrganizationDTO();
        }
        Optional<OrganizationDTO> dtoOptional = organizations.stream().filter(organizationDTO -> organizationDTO.getName().equals(depName))
                .findFirst();
        return dtoOptional.orElseGet(OrganizationDTO::new);
    }

    private String getInspectTargetCategoryEnum(String name) {
        if ("asset".equals(name)) {
            return InspectTargetCategoryEnum.PROPERTY.name();
        }
        return InspectTargetCategoryEnum.SITE_POINT.name();
    }

    private String getShArea(String id, List<AreaDTO> areaDTOS) {
        Optional<AreaDTO> optionalAreaDTO = null;
        if (ObjectUtils.isNotEmpty(id)) {
            InspectionAreaVO shArea = this.inspectSceneTypeMapper.getAllShInspectionArea(id);
            if (Objects.isNull(shArea)) {
                return "";
            }
            optionalAreaDTO = areaDTOS.stream().filter(areaDTO -> areaDTO.getName().equals(shArea.getName()))
                    .findFirst();
        }
        if (!optionalAreaDTO.isPresent()) {
            return "";
        }
        AreaDTO areaDTO = optionalAreaDTO.get();
        List<Long> ids = areaClient.getParentIds(areaDTO.getId());
        if (ObjectUtils.isEmpty(ids)) {
            ids = new ArrayList<>();
        }
        return ids.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

}
