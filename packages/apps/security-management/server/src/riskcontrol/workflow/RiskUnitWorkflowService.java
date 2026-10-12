package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity.RiskAreaEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.service.impl.RiskAreaService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAreaPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAreaParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAreaVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskUnitIdentifyStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskLevelRank;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils.CodeType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskDataPermissionContext;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskExcelExportUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.convert.RiskConfigConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskUnitTypeEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskWarningSignEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskUnitTypeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskWarningSignService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitIdentifierRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitSignRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitIdentifierRelService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitSignRelService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("securityManagementRiskUnitWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskUnitWorkflowService {
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("-180");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("180");
    private static final BigDecimal MIN_LATITUDE = new BigDecimal("-90");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("90");

    private final RiskAreaService areaService;
    private final RiskUnitTypeService unitTypeService;
    private final RiskUnitService unitService;
    private final RiskUnitSignRelService unitSignRelService;
    private final RiskUnitIdentifierRelService unitIdentifierRelService;
    private final RiskWarningSignService warningSignService;
    private final RiskObjectService objectService;
    private final OuaaFacadeService ouaaFacadeService;
    private final FileFacadeService fileFacadeService;
    private final MessageFacadeService messageFacadeService;

    public Pagination<RiskAreaVO> areaPage(RiskAreaPageParam param) {
        List<String> departmentIds = splitValues(param.getDepartmentId());
        List<String> areaTypes = splitValues(param.getAreaType());
        List<String> riskLevels = splitValues(param.getRiskLevel());
        IPage<RiskAreaEntity> page = areaService.page(PagingUtils.convert(param),
                                                      Wrappers.<RiskAreaEntity>lambdaQuery()
                                                              .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                                                                      .like(RiskAreaEntity::getAreaName, param.getKeyword())
                                                                      .or()
                                                                      .like(RiskAreaEntity::getAreaCode, param.getKeyword())
                                                                      .or()
                                                                      .like(RiskAreaEntity::getOwnerUserId, param.getKeyword()))
                                                              .in(CollectionUtils.isNotEmpty(departmentIds), RiskAreaEntity::getDepartmentId, departmentIds)
                                                              .in(CollectionUtils.isNotEmpty(areaTypes), RiskAreaEntity::getAreaType, areaTypes)
                                                              .in(CollectionUtils.isNotEmpty(riskLevels), RiskAreaEntity::getControlRiskLevel, riskLevels)
                                                              .orderByDesc(RiskAreaEntity::getId));
        Pagination<RiskAreaVO> pagination = RiskPageAssembler.toPagination(page, RiskUnitConvert::toVO);
        enrichAreas(pagination.getRecords());
        return pagination;
    }

    public List<RiskOptionVO> areaOptions(RiskAreaPageParam param) {
        return areaService.list(Wrappers.<RiskAreaEntity>lambdaQuery()
                                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskAreaEntity::getAreaName, param.getKeyword()))
                .stream().map(RiskUnitConvert::toOption).toList();
    }

    public RiskAreaVO areaDetail(Long id) {
        RiskAreaEntity entity = areaService.getById(id);
        Assert.isNotFound(entity == null, "区域不存在");
        return enrichArea(RiskUnitConvert.toVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAreaVO createArea(RiskAreaParam param) {
        Long id = IdWorker.getId();
        param.setCode(RiskControlCodeUtils.resolve(param.getCode(), CodeType.AREA, id));
        assertAreaCodeAvailable(param.getCode(), null);
        RiskAreaEntity entity = RiskUnitConvert.toAreaEntity(param);
        entity.setId(id);
        areaService.save(entity);
        return enrichArea(RiskUnitConvert.toVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAreaVO updateArea(Long id, RiskAreaParam param) {
        RiskAreaEntity entity = areaService.getById(id);
        Assert.isNotFound(entity == null, "区域不存在");
        param.setCode(RiskControlCodeUtils.resolveForUpdate(param.getCode(), entity.getAreaCode()));
        assertAreaCodeAvailable(param.getCode(), id);
        RiskUnitConvert.updateAreaEntity(entity, param);
        areaService.updateById(entity);
        return enrichArea(RiskUnitConvert.toVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteArea(Long id) {
        Assert.isConflict(unitService.count(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                    .eq(RiskUnitEntity::getAreaId, id)) > 0, "区域已被风险单元引用");
        DatabaseContextHolder.softDelete(areaService, id);
    }

    public Pagination<RiskUnitVO> unitPage(RiskUnitPageParam param) {
        String identifyStatus = normalizeIdentifyStatus(param.getStatus());
        IPage<RiskUnitEntity> page = unitService.page(PagingUtils.convert(param),
                                                      Wrappers.<RiskUnitEntity>lambdaQuery()
                                                              .like(StringUtils.isNotBlank(param.getKeyword()), RiskUnitEntity::getUnitName, param.getKeyword())
                                                              .eq(param.getAreaId() != null, RiskUnitEntity::getAreaId, param.getAreaId())
                                                              .eq(param.getUnitTypeId() !=
                                                                  null, RiskUnitEntity::getUnitTypeId, param.getUnitTypeId())
                                                              .eq(StringUtils.isNotBlank(param.getResponsibleUserId()),
                                                                  RiskUnitEntity::getResponsibleUserId, param.getResponsibleUserId())
                                                              .eq(StringUtils.isNotBlank(identifyStatus), RiskUnitEntity::getIdentifyStatus, identifyStatus)
                                                              .eq(StringUtils.isNotBlank(param.getRiskLevel()), RiskUnitEntity::getRiskLevel, param.getRiskLevel())
                                                              .orderByDesc(RiskUnitEntity::getId));
        Pagination<RiskUnitVO> pagination = RiskPageAssembler.toPagination(page, RiskUnitConvert::toVO);
        enrichUnitCounts(pagination.getRecords());
        enrichUnits(pagination.getRecords());
        return pagination;
    }

    public List<RiskOptionVO> unitOptions(RiskUnitPageParam param) {
        return unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskUnitEntity::getUnitName, param.getKeyword())
                                        .eq(param.getAreaId() != null, RiskUnitEntity::getAreaId, param.getAreaId())
                                        .eq(param.getUnitTypeId() != null, RiskUnitEntity::getUnitTypeId, param.getUnitTypeId()))
                .stream().map(RiskUnitConvert::toOption).toList();
    }

    public void exportUnits(RiskUnitPageParam param, HttpServletResponse response) {
        List<Map<String, Object>> rows = unitPage(param).getRecords().stream().map(unit -> {
            Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("单元编码", unit.getCode());
            row.put("单元名称", unit.getName());
            row.put("所属区域", unit.getArea() == null ? "" : unit.getArea().getName());
            row.put("单元类型", unit.getUnitType() == null ? "" : unit.getUnitType().getName());
            row.put("责任人员", unit.getResponsibleUser() == null ? unit.getResponsibleUserId() : unit.getResponsibleUser().getUserName());
            row.put("辨识人员", unit.getIdentifierUsers() == null ? "" : unit.getIdentifierUsers().stream()
                    .map(user -> StringUtils.isBlank(user.getUserName()) ? user.getUserId() : user.getUserName())
                    .collect(Collectors.joining("、")));
            row.put("固有风险评估", unit.getInherentRiskLevel());
            row.put("现状风险评估", unit.getControlRiskLevel());
            row.put("辨识状态", unit.getIdentifyStatus());
            return row;
        }).toList();
        RiskExcelExportUtils.write(response, "风险单元列表", rows);
    }

    public Pagination<RiskUnitVO> identificationUnitPage(RiskUnitPageParam param) {
        String identifyStatus = normalizeIdentifyStatus(param.getStatus());
        List<Long> permittedUnitIds = permittedIdentificationUnitIds();
        if (CollectionUtils.isEmpty(permittedUnitIds)) {
            return Pagination.empty();
        }
        IPage<RiskUnitEntity> page = RiskDataPermissionContext.withoutDataPermission(() ->
                unitService.page(PagingUtils.convert(param),
                                 Wrappers.<RiskUnitEntity>lambdaQuery()
                                         .in(RiskUnitEntity::getId, permittedUnitIds)
                                         .like(StringUtils.isNotBlank(param.getKeyword()), RiskUnitEntity::getUnitName, param.getKeyword())
                                         .eq(param.getAreaId() != null, RiskUnitEntity::getAreaId, param.getAreaId())
                                         .eq(param.getUnitTypeId() != null, RiskUnitEntity::getUnitTypeId, param.getUnitTypeId())
                                         .eq(StringUtils.isNotBlank(param.getResponsibleUserId()),
                                             RiskUnitEntity::getResponsibleUserId, param.getResponsibleUserId())
                                         .eq(StringUtils.isNotBlank(identifyStatus), RiskUnitEntity::getIdentifyStatus, identifyStatus)
                                         .eq(StringUtils.isNotBlank(param.getRiskLevel()), RiskUnitEntity::getRiskLevel, param.getRiskLevel())
                                         .orderByDesc(RiskUnitEntity::getId)));
        Pagination<RiskUnitVO> pagination = RiskPageAssembler.toPagination(page, RiskUnitConvert::toVO);
        RiskDataPermissionContext.withoutDataPermission(() -> {
            enrichUnitCounts(pagination.getRecords());
            enrichUnits(pagination.getRecords());
        });
        return pagination;
    }

    public RiskUnitVO unitDetail(Long id) {
        RiskUnitEntity unit = unitService.getById(id);
        Assert.isNotFound(unit == null, "风险单元不存在");
        RiskUnitVO vo = RiskUnitConvert.toVO(unit);
        vo.setIdentifierUserIds(unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                                                                      .eq(RiskUnitIdentifierRelEntity::getUnitId, id))
                                        .stream().map(RiskUnitIdentifierRelEntity::getUserId).toList());
        enrichUnitCounts(List.of(vo));
        return enrichUnit(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskUnitVO createUnit(RiskUnitParam param) {
        validateUnitParam(param);
        Long id = IdWorker.getId();
        param.setCode(RiskControlCodeUtils.resolve(param.getCode(), CodeType.UNIT, id));
        assertUnitCodeAvailable(param.getCode(), null);
        assertUnitNameAvailable(param.getAreaId(), param.getName(), null);
        RiskUnitEntity entity = RiskUnitConvert.toUnitEntity(param);
        entity.setId(id);
        unitService.save(entity);
        saveUnitRelations(entity.getId(), param);
        refreshAreaRiskLevel(Arrays.asList(entity.getAreaId()));
        messageFacadeService.sendUnitAssigned(entity.getId(), param.getIdentifierUserIds());
        return unitDetail(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskUnitVO updateUnit(Long id, RiskUnitParam param) {
        RiskUnitEntity entity = unitService.getById(id);
        Assert.isNotFound(entity == null, "风险单元不存在");
        Long previousAreaId = entity.getAreaId();
        Set<String> previousIdentifierUserIds = identifierUserIds(id);
        validateUnitParam(param);
        param.setCode(RiskControlCodeUtils.resolveForUpdate(param.getCode(), entity.getUnitCode()));
        assertUnitCodeAvailable(param.getCode(), id);
        assertUnitNameAvailable(param.getAreaId(), param.getName(), id);
        RiskUnitConvert.updateUnitEntity(entity, param);
        unitService.updateById(entity);
        replaceUnitRelations(id, param);
        refreshAreaRiskLevel(Arrays.asList(previousAreaId, entity.getAreaId()));
        List<String> addedIdentifierUserIds = param.getIdentifierUserIds().stream()
                .filter(userId -> !previousIdentifierUserIds.contains(userId))
                .toList();
        if (CollectionUtils.isNotEmpty(addedIdentifierUserIds)) {
            messageFacadeService.sendUnitAssigned(id, addedIdentifierUserIds);
        }
        return unitDetail(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUnit(Long id) {
        RiskUnitEntity entity = unitService.getById(id);
        Assert.isNotFound(entity == null, "风险单元不存在");
        Assert.isConflict(objectService.count(Wrappers.<RiskObjectEntity>lambdaQuery()
                                                      .eq(RiskObjectEntity::getUnitId, id)) > 0, "风险单元已被风险对象引用");
        clearUnitRelations(id);
        DatabaseContextHolder.softDelete(unitService, id);
        refreshAreaRiskLevel(Arrays.asList(entity.getAreaId()));
    }

    private RiskAreaVO enrichArea(RiskAreaVO area) {
        enrichAreas(List.of(area));
        return area;
    }

    private void enrichAreas(List<RiskAreaVO> areas) {
        if (CollectionUtils.isEmpty(areas)) {
            return;
        }
        List<Long> areaIds = areas.stream().map(RiskAreaVO::getId).filter(Objects::nonNull).toList();
        Map<Long, List<RiskUnitEntity>> areaUnitMap = areaUnits(areaIds);
        Set<String> userIds = new LinkedHashSet<>();
        Set<String> departmentIds = new LinkedHashSet<>();
        areas.forEach(area -> {
            RiskControlRefUtils.addId(departmentIds, area.getDepartmentId());
            RiskControlRefUtils.addId(userIds, area.getOwnerUserId());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        Map<String, String> departmentNames = ouaaFacadeService.batchDepartmentNames(departmentIds);
        areas.forEach(area -> {
            area.setDepartment(RiskControlRefUtils.toDepartment(area.getDepartmentId(), departmentNames));
            area.setOwnerUser(RiskControlRefUtils.toUser(area.getOwnerUserId(), userNames));
            List<RiskUnitEntity> units = areaUnitMap.getOrDefault(area.getId(), List.of());
            area.setUnitCount((long) units.size());
        });
    }

    private Map<Long, List<RiskUnitEntity>> areaUnits(List<Long> areaIds) {
        if (areaIds != null && CollectionUtils.isEmpty(areaIds)) {
            return Map.of();
        }
        return unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                                        .isNotNull(RiskUnitEntity::getAreaId)
                                        .in(CollectionUtils.isNotEmpty(areaIds), RiskUnitEntity::getAreaId, areaIds))
                .stream()
                .collect(Collectors.groupingBy(RiskUnitEntity::getAreaId));
    }

    private List<String> splitValues(String value) {
        if (StringUtils.isBlank(value)) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
    }

    private String normalizeIdentifyStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return switch (status.trim()) {
            case "PENDING_IDENTIFY", "TO_IDENTIFY", "待辨识", "辨识中", "IDENTIFYING" ->
                    RiskUnitIdentifyStatusEnum.IDENTIFYING.name();
            case "PENDING_REVIEW", "REVIEWING", "待审核", "审核中" -> RiskUnitIdentifyStatusEnum.PENDING_REVIEW.name();
            case "REVIEWED", "已审核" -> RiskUnitIdentifyStatusEnum.REVIEWED.name();
            default -> status.trim();
        };
    }

    private RiskUnitVO enrichUnit(RiskUnitVO unit) {
        enrichUnits(List.of(unit));
        return unit;
    }

    private void enrichUnits(List<RiskUnitVO> units) {
        if (CollectionUtils.isEmpty(units)) {
            return;
        }
        enrichUnitIdentifierUsers(units);
        List<Long> areaIds = units.stream().map(RiskUnitVO::getAreaId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskOptionVO> areaMap = CollectionUtils.isEmpty(areaIds) ? Map.of()
                                                                           : areaService.listByIds(areaIds).stream()
                                                  .collect(Collectors.toMap(
                                                          RiskAreaEntity::getId, RiskUnitConvert::toOption, (left, right) -> left));
        List<Long> unitTypeIds = units.stream().map(RiskUnitVO::getUnitTypeId)
                .filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskOptionVO> unitTypeMap = CollectionUtils.isEmpty(unitTypeIds) ? Map.of()
                                                                                   : unitTypeService.listByIds(unitTypeIds).stream()
                                                      .collect(Collectors.toMap(
                                                              RiskUnitTypeEntity::getId, RiskConfigConvert::toOption, (left, right) -> left));
        Set<String> userIds = new LinkedHashSet<>();
        units.forEach(unit -> {
            RiskControlRefUtils.addId(userIds, unit.getResponsibleUserId());
            RiskControlRefUtils.addIds(userIds, unit.getIdentifierUserIds());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        units.forEach(unit -> {
            if (Objects.nonNull(unit.getAreaId()) && areaMap.containsKey(unit.getAreaId())) {
                unit.setArea(areaMap.get(unit.getAreaId()));
            }
            if (Objects.nonNull(unit.getUnitTypeId()) && unitTypeMap.containsKey(unit.getUnitTypeId())) {
                unit.setUnitType(unitTypeMap.get(unit.getUnitTypeId()));
            }
            unit.setResponsibleUser(RiskControlRefUtils.toUser(unit.getResponsibleUserId(), userNames));
            unit.setIdentifierUsers(RiskControlRefUtils.toUsers(unit.getIdentifierUserIds(), userNames));
        });
        enrichUnitWarningSigns(units);
        enrichWarningSignIcons(units);
    }

    private void enrichUnitIdentifierUsers(List<RiskUnitVO> units) {
        List<Long> unitIds = units.stream()
                .filter(unit -> unit.getId() != null && CollectionUtils.isEmpty(unit.getIdentifierUserIds()))
                .map(RiskUnitVO::getId)
                .toList();
        if (CollectionUtils.isEmpty(unitIds)) {
            return;
        }
        List<RiskUnitIdentifierRelEntity> identifierRels = unitIdentifierRelService.list(
                Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                        .in(RiskUnitIdentifierRelEntity::getUnitId, unitIds)
                        .orderByAsc(RiskUnitIdentifierRelEntity::getId));
        Map<Long, List<String>> unitIdentifierMap = identifierRels.stream()
                .filter(rel -> rel.getUnitId() != null && StringUtils.isNotBlank(rel.getUserId()))
                .collect(Collectors.groupingBy(
                        RiskUnitIdentifierRelEntity::getUnitId,
                        Collectors.mapping(rel -> rel.getUserId().trim(),
                                Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), List::copyOf))));
        units.forEach(unit -> {
            if (CollectionUtils.isEmpty(unit.getIdentifierUserIds())) {
                unit.setIdentifierUserIds(unitIdentifierMap.getOrDefault(unit.getId(), List.of()));
            }
        });
    }

    private void enrichUnitWarningSigns(List<RiskUnitVO> units) {
        List<Long> unitIds = units.stream().map(RiskUnitVO::getId).filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(unitIds)) {
            return;
        }
        List<RiskUnitSignRelEntity> signRels = unitSignRelService.list(Wrappers.<RiskUnitSignRelEntity>lambdaQuery()
                                                                               .in(RiskUnitSignRelEntity::getUnitId, unitIds)
                                                                               .orderByDesc(RiskUnitSignRelEntity::getId));
        if (CollectionUtils.isEmpty(signRels)) {
            return;
        }
        Map<Long, Long> unitSignIdMap = signRels.stream()
                .filter(rel -> rel.getUnitId() != null && rel.getSignId() != null)
                .collect(Collectors.toMap(RiskUnitSignRelEntity::getUnitId, RiskUnitSignRelEntity::getSignId, (left, right) -> left));
        List<Long> warningSignIdValues = unitSignIdMap.values().stream().distinct().toList();
        Map<Long, RiskWarningSignVO> signMap = CollectionUtils.isEmpty(warningSignIdValues) ? Map.of()
                                                                                        : warningSignService.listByIds(warningSignIdValues)
                                                   .stream()
                                                   .collect(Collectors.toMap(
                                                           RiskWarningSignEntity::getId,
                                                           this::toWarningSignVO,
                                                           (left, right) -> left));
        units.forEach(unit -> {
            RiskWarningSignVO sign = signMap.get(unitSignIdMap.get(unit.getId()));
            if (sign != null) {
                unit.setWarningSignId(sign.getId());
                unit.setWarningSign(sign);
            }
        });
    }

    private void enrichWarningSignIcons(List<RiskUnitVO> units) {
        List<RiskWarningSignVO> signs = units.stream()
                .map(RiskUnitVO::getWarningSign)
                .filter(Objects::nonNull)
                .toList();
        if (CollectionUtils.isEmpty(signs)) {
            return;
        }
        Set<String> fileIds = new LinkedHashSet<>();
        signs.forEach(sign -> addFileId(fileIds, sign.getIconFileId()));
        Map<String, FileVO> fileMap = fileFacadeService.batchFiles(fileIds);
        signs.forEach(sign -> sign.setIconFile(fileMap.get(normalizeFileId(sign.getIconFileId()))));
    }

    private void addFileId(Set<String> fileIds, String fileId) {
        if (StringUtils.isNotBlank(fileId)) {
            fileIds.add(normalizeFileId(fileId));
        }
    }

    private String normalizeFileId(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return null;
        }
        try {
            return String.valueOf(Long.valueOf(fileId.trim()));
        } catch (NumberFormatException ex) {
            return fileId.trim();
        }
    }

    private void saveUnitRelations(Long unitId, RiskUnitParam param) {
        if (param.getWarningSignId() != null) {
            RiskUnitSignRelEntity signRel = RiskUnitConvert.toSignRel(unitId, param.getWarningSignId());
            signRel.setId(IdWorker.getId());
            unitSignRelService.save(signRel);
        }
        if (CollectionUtils.isNotEmpty(param.getIdentifierUserIds())) {
            unitIdentifierRelService.saveBatch(param.getIdentifierUserIds().stream()
                                                       .map(userId -> RiskUnitConvert.toIdentifierRel(unitId, userId))
                                                       .peek(rel -> rel.setId(IdWorker.getId()))
                                                       .toList());
        }
    }

    private void assertAreaCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(areaService.count(Wrappers.<RiskAreaEntity>lambdaQuery()
                                                    .eq(RiskAreaEntity::getAreaCode, code)
                                                    .ne(excludeId != null, RiskAreaEntity::getId, excludeId)) > 0, "区域编号已存在");
    }

    private void assertUnitCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(unitService.count(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                    .eq(RiskUnitEntity::getUnitCode, code)
                                                    .ne(excludeId != null, RiskUnitEntity::getId, excludeId)) > 0, "风险单元编号已存在");
    }

    private void assertUnitNameAvailable(Long areaId, String name, Long excludeId) {
        Assert.isConflict(unitService.count(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                    .eq(RiskUnitEntity::getAreaId, areaId)
                                                    .eq(RiskUnitEntity::getUnitName, name)
                                                    .ne(excludeId != null, RiskUnitEntity::getId, excludeId)) >
                          0, "同一区域下风险单元名称已存在");
    }

    private void clearUnitRelations(Long unitId) {
        List<RiskUnitSignRelEntity> signRels = unitSignRelService.list(Wrappers.<RiskUnitSignRelEntity>lambdaQuery()
                                                                               .eq(RiskUnitSignRelEntity::getUnitId, unitId));
        if (CollectionUtils.isNotEmpty(signRels)) {
            DatabaseContextHolder.softDelete(unitSignRelService, signRels);
        }
        List<RiskUnitIdentifierRelEntity> identifierRels = unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                                                                                                 .eq(RiskUnitIdentifierRelEntity::getUnitId, unitId));
        if (CollectionUtils.isNotEmpty(identifierRels)) {
            DatabaseContextHolder.softDelete(unitIdentifierRelService, identifierRels);
        }
    }

    private void replaceUnitRelations(Long unitId, RiskUnitParam param) {
        clearUnitRelations(unitId);
        saveUnitRelations(unitId, param);
    }

    private Set<String> identifierUserIds(Long unitId) {
        if (unitId == null) {
            return Set.of();
        }
        return unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                        .eq(RiskUnitIdentifierRelEntity::getUnitId, unitId))
                .stream()
                .map(RiskUnitIdentifierRelEntity::getUserId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    private List<Long> permittedIdentificationUnitIds() {
        return unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                        .isNotNull(RiskUnitIdentifierRelEntity::getUnitId))
                .stream()
                .map(RiskUnitIdentifierRelEntity::getUnitId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private void validateUnitParam(RiskUnitParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getName()), "单元名称不能为空");
        param.setName(param.getName().trim());
        Assert.isParameterError(param.getName().length() > 50, "单元名称最多50字符");
        Assert.isParameterError(param.getAreaId() == null, "所属区域不能为空");
        Assert.isNotFound(areaService.getById(param.getAreaId()) == null, "所属区域不存在");
        Assert.isParameterError(param.getUnitTypeId() == null, "单元类型不能为空");
        Assert.isNotFound(unitTypeService.getById(param.getUnitTypeId()) == null, "单元类型不存在");
        Assert.isParameterError(StringUtils.isBlank(param.getGisPoint()), "所在位置不能为空");
        validateCoordinate(param.getLongitude(), MIN_LONGITUDE, MAX_LONGITUDE, "经度");
        validateCoordinate(param.getLatitude(), MIN_LATITUDE, MAX_LATITUDE, "纬度");
        if (StringUtils.isNotBlank(param.getDescription())) {
            param.setDescription(param.getDescription().trim());
            Assert.isParameterError(param.getDescription().length() > 500, "风险单元描述最多500字符");
        }
        if (StringUtils.isNotBlank(param.getLocationDesc())) {
            param.setLocationDesc(param.getLocationDesc().trim());
            Assert.isParameterError(param.getLocationDesc().length() > 300, "位置描述最多300字符");
        }
        Assert.isParameterError(param.getWarningSignId() == null, "安全警示标志不能为空");
        RiskWarningSignEntity warningSign = warningSignService.getById(param.getWarningSignId());
        Assert.isNotFound(warningSign == null, "安全警示标志不存在");
        Assert.isConflict(Boolean.FALSE.equals(warningSign.getEnabled()), "安全警示标志已停用");
        Assert.isParameterError(param.getIdentifyCycleDays() == null || param.getIdentifyCycleDays() <= 0, "辨识周期必须为正整数");
        List<String> identifierUserIds = cleanUserIds(param.getIdentifierUserIds());
        Assert.isParameterError(CollectionUtils.isEmpty(identifierUserIds), "辨识人员不能为空");
        param.setIdentifierUserIds(identifierUserIds);
        Assert.isParameterError(StringUtils.isBlank(param.getResponsibleUserId()), "责任人员不能为空");
        param.setResponsibleUserId(param.getResponsibleUserId().trim());
        param.setWarningSignId(warningSign.getId());
        param.setSignId(warningSign.getId());
    }

    private List<String> cleanUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return List.of();
        }
        return userIds.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .toList();
    }

    private void validateCoordinate(BigDecimal value, BigDecimal min, BigDecimal max, String fieldName) {
        if (value == null) {
            return;
        }
        Assert.isParameterError(value.compareTo(min) < 0 || value.compareTo(max) > 0,
                                fieldName + "超出有效范围");
    }

    private RiskWarningSignVO toWarningSignVO(RiskWarningSignEntity sign) {
        RiskWarningSignVO signVO = new RiskWarningSignVO();
        signVO.setId(sign.getId());
        signVO.setCode(sign.getSignCode());
        signVO.setName(sign.getSignName());
        signVO.setSignType(sign.getSignType());
        signVO.setIconFileId(sign.getIconFileId());
        signVO.setEnabled(sign.getEnabled());
        return signVO;
    }

    private void enrichUnitCounts(List<RiskUnitVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> unitIds = unitVOs.stream().map(RiskUnitVO::getId).filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(unitIds)) {
            return;
        }
        List<RiskObjectEntity> objects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                                                                    .in(RiskObjectEntity::getUnitId, unitIds));
        Map<Long, List<RiskObjectEntity>> objectMap = objects.stream()
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        Map<Long, Long> objectCountMap = objects.stream()
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId, Collectors.counting()));
        Map<Long, Long> rejectedCountMap = objects.stream()
                .filter(object -> Objects.equals(object.getStatus(), "DRAFT"))
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId, Collectors.counting()));
        List<RiskUnitEntity> riskLevelUpdates = unitVOs.stream()
                .map(vo -> {
                    List<RiskObjectEntity> unitObjects = objectMap.getOrDefault(vo.getId(), List.of());
                    String previousInherentRiskLevel = vo.getInherentRiskLevel();
                    String previousControlRiskLevel = vo.getControlRiskLevel();
                    String previousRiskLevel = vo.getRiskLevel();
                    String inherentRiskLevel = RiskLevelRank.highest(unitObjects.stream()
                            .map(RiskObjectEntity::getInherentRiskLevel)
                            .toList());
                    String controlRiskLevel = RiskLevelRank.highest(unitObjects.stream()
                            .map(RiskObjectEntity::getControlRiskLevel)
                            .toList());
                    String riskLevel = RiskLevelRank.highest(unitObjects.stream()
                            .map(RiskUnitWorkflowService::currentObjectRiskLevel)
                            .toList());
                    vo.setInherentRiskLevel(inherentRiskLevel);
                    vo.setControlRiskLevel(controlRiskLevel);
                    vo.setRiskLevel(riskLevel);
                    if (Objects.equals(previousInherentRiskLevel, inherentRiskLevel)
                        && Objects.equals(previousControlRiskLevel, controlRiskLevel)
                        && Objects.equals(previousRiskLevel, riskLevel)) {
                        return null;
                    }
                    RiskUnitEntity unit = new RiskUnitEntity();
                    unit.setId(vo.getId());
                    unit.setAreaId(vo.getAreaId());
                    unit.setInherentRiskLevel(inherentRiskLevel);
                    unit.setControlRiskLevel(controlRiskLevel);
                    unit.setRiskLevel(riskLevel);
                    return unit;
                })
                .filter(Objects::nonNull)
                .toList();
        unitVOs.forEach(vo -> {
            vo.setObjectCount(objectCountMap.getOrDefault(vo.getId(), 0L));
            vo.setRejectedCount(rejectedCountMap.getOrDefault(vo.getId(), 0L));
        });
        if (CollectionUtils.isNotEmpty(riskLevelUpdates)) {
            unitService.updateBatchById(riskLevelUpdates);
            refreshAreaRiskLevel(riskLevelUpdates.stream()
                                                 .map(RiskUnitEntity::getAreaId)
                                                 .filter(Objects::nonNull)
                                                 .distinct()
                                                 .toList());
        }
    }

    private static String currentObjectRiskLevel(RiskObjectEntity object) {
        if (object == null) {
            return null;
        }
        return StringUtils.isNotBlank(object.getControlRiskLevel()) ? object.getControlRiskLevel() : object.getInherentRiskLevel();
    }

    private void refreshAreaRiskLevel(List<Long> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            return;
        }
        List<Long> filteredAreaIds = areaIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(filteredAreaIds)) {
            return;
        }
        List<RiskAreaEntity> areas = areaService.listByIds(filteredAreaIds).stream().toList();
        if (CollectionUtils.isEmpty(areas)) {
            return;
        }
        List<RiskUnitEntity> units = unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                              .in(RiskUnitEntity::getAreaId, filteredAreaIds));
        Map<Long, List<RiskUnitEntity>> unitMap = units.stream().collect(Collectors.groupingBy(RiskUnitEntity::getAreaId));
        areas.forEach(area -> {
            List<RiskUnitEntity> areaUnits = unitMap.getOrDefault(area.getId(), List.of());
            String inherentRiskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskUnitEntity::getInherentRiskLevel).toList());
            String controlRiskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskUnitEntity::getControlRiskLevel).toList());
            String riskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskUnitWorkflowService::currentUnitRiskLevel).toList());
            areaService.update(Wrappers.<RiskAreaEntity>lambdaUpdate()
                                       .eq(RiskAreaEntity::getId, area.getId())
                                       .set(RiskAreaEntity::getInherentRiskLevel, inherentRiskLevel)
                                       .set(RiskAreaEntity::getControlRiskLevel, controlRiskLevel)
                                       .set(RiskAreaEntity::getRiskLevel, riskLevel));
        });
    }

    private static String currentUnitRiskLevel(RiskUnitEntity unit) {
        if (unit == null) {
            return null;
        }
        if (StringUtils.isNotBlank(unit.getRiskLevel())) {
            return unit.getRiskLevel();
        }
        return StringUtils.isNotBlank(unit.getControlRiskLevel()) ? unit.getControlRiskLevel() : unit.getInherentRiskLevel();
    }

}
