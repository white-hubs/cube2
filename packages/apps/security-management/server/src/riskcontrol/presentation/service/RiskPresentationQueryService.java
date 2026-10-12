package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity.RiskAreaEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.service.impl.RiskAreaService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskFourColorUnitPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskNoticeCardPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitInventoryPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskDepartmentRefVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskFourColorUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskNoticeCardVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitInventoryVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUserRefVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskMeasureExecutionStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskLevelRank;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskExcelExportUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskFourColorStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.convert.RiskConfigConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.dao.RiskWarningSignMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskUnitTypeEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskWarningSignEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskUnitTypeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.HiddenDangerFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.convert.RiskIdentificationConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.dao.RiskObjectMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.convert.RiskMeasureConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.dao.RiskControlMeasureMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskControlMeasureEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.convert.RiskPresentationConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.dao.RiskUnitIdentifierRelMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.dao.RiskUnitMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.dao.RiskUnitSignRelMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitIdentifierRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitSignRelEntity;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("securityManagementRiskPresentationQueryService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskPresentationQueryService {
    private final RiskUnitMapper unitMapper;
    private final RiskAreaService areaService;
    private final RiskUnitTypeService unitTypeService;
    private final RiskObjectMapper objectMapper;
    private final RiskControlMeasureMapper measureMapper;
    private final RiskUnitSignRelMapper unitSignRelMapper;
    private final RiskUnitIdentifierRelMapper unitIdentifierRelMapper;
    private final RiskWarningSignMapper warningSignMapper;
    private final HiddenDangerFacadeService hiddenDangerFacadeService;
    private final FileFacadeService fileFacadeService;
    private final OuaaFacadeService ouaaFacadeService;

    private InventoryQueryCriteria toCriteria(RiskUnitInventoryPageParam param) {
        return new InventoryQueryCriteria(param.getKeyword(), null, param.getAreaId(), param.getUnitTypeId(),
                param.getRiskLevel(), param.getControlStatus(), null);
    }

    private InventoryQueryCriteria toCriteria(RiskFourColorUnitPageParam param) {
        return new InventoryQueryCriteria(param.getKeyword(), null, param.getAreaId(), param.getUnitTypeId(),
                param.getRiskLevel(), null, null);
    }

    private InventoryQueryCriteria toCriteria(RiskNoticeCardPageParam param) {
        return new InventoryQueryCriteria(param.getKeyword(), param.getUnitId(), null, null,
                param.getRiskLevel(), null, param.getDepartmentId());
    }

    private <S, T> Pagination<T> mapPage(Pagination<S> source, Function<S, T> mapper) {
        return new Pagination<>(source.getTotal(), source.getPages(),
                source.getRecords().stream().map(mapper).toList());
    }

    public Pagination<RiskUnitInventoryVO> unitInventory(RiskUnitInventoryPageParam param) {
        return unitInventoryPage(param, toCriteria(param));
    }

    private Pagination<RiskUnitInventoryVO> unitInventoryPage(Paging paging, InventoryQueryCriteria criteria) {
        List<Long> departmentAreaIds = areaIdsByDepartments(criteria.departmentId());
        if (StringUtils.isNotBlank(criteria.departmentId()) && CollectionUtils.isEmpty(departmentAreaIds)) {
            return Pagination.empty();
        }
        IPage<RiskUnitEntity> page = unitMapper.selectPage(PagingUtils.convert(paging),
                unitInventoryQuery(criteria, departmentAreaIds));
        Pagination<RiskUnitInventoryVO> pagination = RiskPageAssembler.toPagination(page, RiskPresentationConvert::toUnitInventoryVO);
        enrichInventory(pagination.getRecords());
        return pagination;
    }

    public void exportUnitInventory(RiskUnitInventoryPageParam param, HttpServletResponse response) {
        List<RiskUnitInventoryVO> unitVOs = unitInventoryList(toCriteria(param));
        List<Map<String, Object>> rows = unitVOs.stream().map(vo -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("风险单元ID", vo.getUnitId());
            row.put("风险单元编号", vo.getCode());
            row.put("风险单元名称", vo.getName());
            row.put("单元类型", optionName(vo.getUnitType()));
            row.put("所属区域", optionName(vo.getArea()));
            row.put("所在位置", vo.getLocationDesc());
            row.put("单元描述", vo.getDescription());
            row.put("安全警示标志", warningSignName(vo));
            row.put("辨识周期", vo.getIdentifyCycleDays());
            row.put("辨识人员", userNames(vo.getIdentifierUsers()));
            row.put("责任人", userName(vo.getResponsibleUser()));
            row.put("固有风险等级", vo.getInherentRiskLevel());
            row.put("控制风险等级", vo.getControlRiskLevel());
            row.put("管控状态", vo.getControlStatus());
            row.put("风险对象数", vo.getObjectCount());
            row.put("管控措施数", vo.getMeasureCount());
            row.put("已完成措施数", vo.getCompletedMeasureCount());
            row.put("活跃隐患数", vo.getHiddenDangerCount());
            row.put("下次辨识时间", vo.getNextIdentifyTime());
            return row;
        }).toList();
        RiskExcelExportUtils.write(response, "风险单元清单", rows);
    }

    public List<RiskObjectVO> unitInventoryObjects(Long unitId) {
        List<RiskObjectEntity> objects = objectMapper.selectList(Wrappers.<RiskObjectEntity>lambdaQuery()
                .eq(RiskObjectEntity::getUnitId, unitId));
        List<Long> objectIds = objects.stream().map(RiskObjectEntity::getId).toList();
        Map<Long, List<RiskControlMeasureEntity>> measureMap = CollectionUtils.isEmpty(objectIds) ? Map.of()
                : measureMapper.selectList(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .in(RiskControlMeasureEntity::getObjectId, objectIds))
                .stream().collect(Collectors.groupingBy(RiskControlMeasureEntity::getObjectId));
        List<RiskObjectVO> vos = objects.stream().map(object -> {
            RiskObjectVO vo = RiskIdentificationConvert.toVO(object);
            vo.setMeasures(measureMap.getOrDefault(object.getId(), List.of()).stream().map(RiskMeasureConvert::toVO).toList());
            return vo;
        }).toList();
        enrichObjectAndMeasureUsers(vos);
        return vos;
    }

    public RiskFourColorStatisticsVO fourColorStatistics() {
        Map<RiskLevelEnum, Long> stats = new EnumMap<>(RiskLevelEnum.class);
        for (RiskLevelEnum level : RiskLevelEnum.values()) {
            stats.put(level, 0L);
        }
        unitMapper.selectList(Wrappers.<RiskUnitEntity>lambdaQuery()).stream()
                .map(RiskUnitEntity::getControlRiskLevel)
                .filter(Objects::nonNull)
                .map(RiskPresentationQueryService::parseRiskLevel)
                .filter(Objects::nonNull)
                .forEach(level -> stats.compute(level, (key, value) -> value == null ? 1L : value + 1));
        RiskFourColorStatisticsVO vo = new RiskFourColorStatisticsVO();
        vo.setMajorCount(stats.getOrDefault(RiskLevelEnum.MAJOR, 0L));
        vo.setLargeCount(stats.getOrDefault(RiskLevelEnum.LARGE, 0L));
        vo.setGeneralCount(stats.getOrDefault(RiskLevelEnum.GENERAL, 0L));
        vo.setLowCount(stats.getOrDefault(RiskLevelEnum.LOW, 0L));
        vo.setTotalCount(vo.getMajorCount() + vo.getLargeCount() + vo.getGeneralCount() + vo.getLowCount());
        return vo;
    }

    public Pagination<RiskFourColorUnitVO> fourColorUnits(RiskFourColorUnitPageParam param) {
        Pagination<RiskFourColorUnitVO> pagination = mapPage(unitInventoryPage(param, toCriteria(param)),
                this::toFourColorUnitVO);
        enrichFourColorDepartments(pagination.getRecords());
        return pagination;
    }

    public RiskFourColorUnitVO fourColorUnitDetail(Long unitId) {
        RiskFourColorUnitVO vo = toFourColorUnitVO(unitInventoryDetail(unitId));
        vo.setObjects(unitInventoryObjects(unitId));
        enrichFourColorDepartments(List.of(vo));
        return vo;
    }

    public Pagination<RiskNoticeCardVO> noticeCards(RiskNoticeCardPageParam param) {
        Pagination<RiskNoticeCardVO> pagination = mapPage(unitInventoryPage(param, toCriteria(param)),
                this::toNoticeCardVO);
        enrichNoticeCards(pagination.getRecords());
        return pagination;
    }

    public void exportNoticeCards(RiskNoticeCardPageParam param, HttpServletResponse response) {
        List<RiskNoticeCardVO> unitVOs = unitInventoryList(toCriteria(param)).stream()
                .map(this::toNoticeCardVO)
                .toList();
        enrichNoticeCards(unitVOs);
        List<Map<String, Object>> rows = unitVOs.stream().map(vo -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("风险单元ID", vo.getUnitId());
            row.put("风险单元编号", vo.getCode());
            row.put("风险单元名称", vo.getName());
            row.put("所属区域", optionName(vo.getArea()));
            row.put("责任部门", departmentName(vo.getDepartment()));
            row.put("责任人", userName(vo.getResponsibleUser()));
            row.put("固有风险等级", vo.getInherentRiskLevel());
            row.put("现状风险等级", vo.getControlRiskLevel());
            row.put("警示标志", vo.getWarningSigns() == null ? "" : vo.getWarningSigns().stream()
                    .map(RiskWarningSignVO::getName).filter(Objects::nonNull).collect(Collectors.joining(",")));
            row.put("风险对象数", vo.getObjects() == null ? 0 : vo.getObjects().size());
            row.put("管控措施数", vo.getObjects() == null ? 0 : vo.getObjects().stream()
                    .mapToInt(object -> object.getMeasures() == null ? 0 : object.getMeasures().size()).sum());
            return row;
        }).toList();
        RiskExcelExportUtils.write(response, "风险告知卡", rows);
    }

    public RiskNoticeCardVO noticeCardDetail(Long unitId) {
        RiskNoticeCardVO vo = toNoticeCardVO(unitInventoryDetail(unitId));
        enrichNoticeCards(List.of(vo));
        return vo;
    }

    private RiskFourColorUnitVO toFourColorUnitVO(RiskUnitInventoryVO source) {
        RiskFourColorUnitVO target = new RiskFourColorUnitVO();
        target.setId(source.getId());
        target.setUnitId(source.getUnitId());
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setAreaId(source.getAreaId());
        target.setArea(source.getArea());
        target.setUnitTypeId(source.getUnitTypeId());
        target.setUnitType(source.getUnitType());
        target.setResponsibleUserId(source.getResponsibleUserId());
        target.setResponsibleUser(source.getResponsibleUser());
        target.setInherentRiskLevel(source.getInherentRiskLevel());
        target.setControlRiskLevel(source.getControlRiskLevel());
        target.setControlStatus(source.getControlStatus());
        target.setNextIdentifyTime(source.getNextIdentifyTime());
        target.setGisPoint(source.getGisPoint());
        target.setObjectCount(source.getObjectCount());
        target.setMeasureCount(source.getMeasureCount());
        target.setHiddenDangerCount(source.getHiddenDangerCount());
        return target;
    }

    private RiskNoticeCardVO toNoticeCardVO(RiskUnitInventoryVO source) {
        RiskNoticeCardVO target = new RiskNoticeCardVO();
        target.setId(source.getId());
        target.setUnitId(source.getUnitId());
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setAreaId(source.getAreaId());
        target.setArea(source.getArea());
        target.setResponsibleUserId(source.getResponsibleUserId());
        target.setResponsibleUser(source.getResponsibleUser());
        target.setInherentRiskLevel(source.getInherentRiskLevel());
        target.setControlRiskLevel(source.getControlRiskLevel());
        target.setControlStatus(source.getControlStatus());
        target.setNextIdentifyTime(source.getNextIdentifyTime());
        target.setObjectCount(source.getObjectCount());
        target.setMeasureCount(source.getMeasureCount());
        target.setHiddenDangerCount(source.getHiddenDangerCount());
        return target;
    }

    private RiskUnitInventoryVO unitInventoryDetail(Long unitId) {
        RiskUnitEntity unit = unitMapper.selectById(unitId);
        Assert.isNotFound(unit == null, "风险单元不存在");
        RiskUnitInventoryVO vo = RiskPresentationConvert.toUnitInventoryVO(unit);
        enrichInventory(List.of(vo));
        return vo;
    }

    private List<RiskUnitInventoryVO> unitInventoryList(InventoryQueryCriteria criteria) {
        List<Long> departmentAreaIds = areaIdsByDepartments(criteria.departmentId());
        if (StringUtils.isNotBlank(criteria.departmentId()) && CollectionUtils.isEmpty(departmentAreaIds)) {
            return List.of();
        }
        List<RiskUnitInventoryVO> unitVOs = unitMapper.selectList(unitInventoryQuery(criteria, departmentAreaIds))
                .stream().map(RiskPresentationConvert::toUnitInventoryVO).toList();
        enrichInventory(unitVOs);
        return unitVOs;
    }

    private LambdaQueryWrapper<RiskUnitEntity> unitInventoryQuery(InventoryQueryCriteria param, List<Long> departmentAreaIds) {
        return Wrappers.<RiskUnitEntity>lambdaQuery()
                .like(StringUtils.isNotBlank(param.keyword()), RiskUnitEntity::getUnitName, param.keyword())
                .eq(param.unitId() != null, RiskUnitEntity::getId, param.unitId())
                .eq(param.areaId() != null, RiskUnitEntity::getAreaId, param.areaId())
                .eq(param.unitTypeId() != null, RiskUnitEntity::getUnitTypeId, param.unitTypeId())
                .in(CollectionUtils.isNotEmpty(departmentAreaIds), RiskUnitEntity::getAreaId, departmentAreaIds)
                .eq(StringUtils.isNotBlank(param.riskLevel()), RiskUnitEntity::getControlRiskLevel, param.riskLevel())
                .eq(StringUtils.isNotBlank(param.controlStatus()), RiskUnitEntity::getControlStatus, param.controlStatus())
                .orderByDesc(RiskUnitEntity::getId);
    }

    private void enrichInventory(List<RiskUnitInventoryVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> unitIds = unitVOs.stream().map(RiskUnitInventoryVO::getUnitId).filter(Objects::nonNull).toList();
        List<RiskObjectEntity> objects = objectMapper.selectList(Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getUnitId, unitIds));
        List<Long> objectIds = objects.stream().map(RiskObjectEntity::getId).toList();
        List<RiskControlMeasureEntity> measures = CollectionUtils.isEmpty(objectIds) ? List.of()
                : measureMapper.selectList(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .in(RiskControlMeasureEntity::getObjectId, objectIds));
        Map<Long, Long> objectCountMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId, Collectors.counting()));
        Map<Long, String> inherentRiskLevelMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId,
                Collectors.collectingAndThen(Collectors.mapping(RiskObjectEntity::getInherentRiskLevel, Collectors.toList()), RiskLevelRank::highest)));
        Map<Long, String> controlRiskLevelMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId,
                Collectors.collectingAndThen(Collectors.mapping(RiskObjectEntity::getControlRiskLevel, Collectors.toList()), RiskLevelRank::highest)));
        Map<Long, List<RiskControlMeasureEntity>> measureMap = measures.stream().collect(Collectors.groupingBy(RiskControlMeasureEntity::getObjectId));
        Map<Long, Long> measureCountMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId,
                Collectors.summingLong(object -> measureMap.getOrDefault(object.getId(), List.of()).size())));
        Map<Long, Long> completedMeasureCountMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId,
                Collectors.summingLong(object -> measureMap.getOrDefault(object.getId(), List.of()).stream()
                        .filter(measure -> Objects.equals(measure.getExecutionStatus(), RiskMeasureExecutionStatusEnum.COMPLETED.name()))
                        .count())));
        Map<Long, Long> hiddenDangerCountMap = hiddenDangerFacadeService.countActiveByUnitIds(unitIds);
        unitVOs.forEach(vo -> {
            vo.setObjectCount(objectCountMap.getOrDefault(vo.getUnitId(), 0L));
            if (inherentRiskLevelMap.containsKey(vo.getUnitId())) {
                vo.setInherentRiskLevel(inherentRiskLevelMap.get(vo.getUnitId()));
            }
            if (controlRiskLevelMap.containsKey(vo.getUnitId())) {
                vo.setControlRiskLevel(controlRiskLevelMap.get(vo.getUnitId()));
            }
            vo.setMeasureCount(measureCountMap.getOrDefault(vo.getUnitId(), 0L));
            vo.setCompletedMeasureCount(completedMeasureCountMap.getOrDefault(vo.getUnitId(), 0L));
            vo.setHiddenDangerCount(hiddenDangerCountMap.getOrDefault(vo.getUnitId(), 0L));
        });
        enrichUnitRelations(unitVOs);
        enrichUnitRefs(unitVOs);
    }

    private static RiskLevelEnum parseRiskLevel(String riskLevel) {
        if (StringUtils.isBlank(riskLevel)) {
            return null;
        }
        try {
            return RiskLevelEnum.valueOf(riskLevel);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<Long> areaIdsByDepartments(String departmentId) {
        List<String> departmentIds = splitValues(departmentId);
        if (CollectionUtils.isEmpty(departmentIds)) {
            return List.of();
        }
        return areaService.list(Wrappers.<RiskAreaEntity>lambdaQuery()
                        .in(RiskAreaEntity::getDepartmentId, departmentIds))
                .stream()
                .map(RiskAreaEntity::getId)
                .toList();
    }

    private void enrichUnitRefs(List<RiskUnitInventoryVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> areaIds = unitVOs.stream().map(RiskUnitInventoryVO::getAreaId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskAreaEntity> areaMap = CollectionUtils.isEmpty(areaIds) ? Map.of()
                : areaService.listByIds(areaIds).stream().collect(Collectors.toMap(
                RiskAreaEntity::getId, Function.identity(), (left, right) -> left));
        List<Long> unitTypeIds = unitVOs.stream().map(RiskUnitInventoryVO::getUnitTypeId)
                .filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitTypeEntity> unitTypeMap = CollectionUtils.isEmpty(unitTypeIds) ? Map.of()
                : unitTypeService.listByIds(unitTypeIds).stream().collect(Collectors.toMap(
                RiskUnitTypeEntity::getId, Function.identity(), (left, right) -> left));
        Set<String> userIds = new LinkedHashSet<>();
        unitVOs.forEach(vo -> {
            RiskControlRefUtils.addId(userIds, vo.getResponsibleUserId());
            RiskControlRefUtils.addIds(userIds, vo.getIdentifierUserIds());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        unitVOs.forEach(vo -> {
            RiskAreaEntity area = areaMap.get(vo.getAreaId());
            if (area != null) {
                vo.setArea(RiskUnitConvert.toOption(area));
            }
            RiskUnitTypeEntity unitType = unitTypeMap.get(vo.getUnitTypeId());
            if (unitType != null) {
                vo.setUnitType(RiskConfigConvert.toOption(unitType));
            }
            vo.setResponsibleUser(RiskControlRefUtils.toUser(vo.getResponsibleUserId(), userNames));
            vo.setIdentifierUsers(RiskControlRefUtils.toUsers(vo.getIdentifierUserIds(), userNames));
        });
    }

    private void enrichUnitRelations(List<RiskUnitInventoryVO> unitVOs) {
        List<Long> unitIds = unitVOs.stream().map(RiskUnitInventoryVO::getUnitId).filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(unitIds)) {
            return;
        }
        Map<Long, List<String>> unitIdentifierMap = unitIdentifierRelMapper.selectList(
                        Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                                .in(RiskUnitIdentifierRelEntity::getUnitId, unitIds)
                                .orderByAsc(RiskUnitIdentifierRelEntity::getId))
                .stream()
                .filter(rel -> rel.getUnitId() != null && StringUtils.isNotBlank(rel.getUserId()))
                .collect(Collectors.groupingBy(
                        RiskUnitIdentifierRelEntity::getUnitId,
                        Collectors.mapping(rel -> rel.getUserId().trim(),
                                Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), List::copyOf))));
        List<RiskUnitSignRelEntity> signRels = unitSignRelMapper.selectList(Wrappers.<RiskUnitSignRelEntity>lambdaQuery()
                .in(RiskUnitSignRelEntity::getUnitId, unitIds)
                .orderByDesc(RiskUnitSignRelEntity::getId));
        Map<Long, Long> unitSignIdMap = signRels.stream()
                .filter(rel -> rel.getUnitId() != null && rel.getSignId() != null)
                .collect(Collectors.toMap(RiskUnitSignRelEntity::getUnitId, RiskUnitSignRelEntity::getSignId, (left, right) -> left));
        List<Long> signIds = unitSignIdMap.values().stream().distinct().toList();
        Map<Long, RiskWarningSignEntity> signMap = CollectionUtils.isEmpty(signIds) ? Map.of()
                : warningSignMapper.selectBatchIds(signIds).stream().collect(Collectors.toMap(
                        RiskWarningSignEntity::getId, Function.identity(), (left, right) -> left));
        unitVOs.forEach(vo -> {
            vo.setIdentifierUserIds(unitIdentifierMap.getOrDefault(vo.getUnitId(), List.of()));
            RiskWarningSignEntity sign = signMap.get(unitSignIdMap.get(vo.getUnitId()));
            if (sign != null) {
                vo.setWarningSignId(sign.getId());
                vo.setWarningSign(toWarningSignVO(sign));
            }
        });
        enrichWarningSignIcons(unitVOs.stream()
                .map(RiskUnitInventoryVO::getWarningSign)
                .filter(Objects::nonNull)
                .toList());
    }

    private void enrichFourColorDepartments(List<RiskFourColorUnitVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> areaIds = unitVOs.stream().map(RiskFourColorUnitVO::getAreaId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskAreaEntity> areaMap = CollectionUtils.isEmpty(areaIds) ? Map.of()
                : areaService.listByIds(areaIds).stream().collect(Collectors.toMap(
                RiskAreaEntity::getId, Function.identity(), (left, right) -> left));
        Set<String> departmentIds = new LinkedHashSet<>();
        areaMap.values().forEach(area -> RiskControlRefUtils.addId(departmentIds, area.getDepartmentId()));
        Map<String, String> departmentNames = ouaaFacadeService.batchDepartmentNames(departmentIds);
        unitVOs.forEach(vo -> {
            RiskAreaEntity area = areaMap.get(vo.getAreaId());
            if (area == null) {
                return;
            }
            vo.setArea(RiskUnitConvert.toOption(area));
            vo.setDepartmentId(area.getDepartmentId());
            vo.setDepartment(RiskControlRefUtils.toDepartment(area.getDepartmentId(), departmentNames));
        });
    }

    private void enrichNoticeCardDepartments(List<RiskNoticeCardVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> areaIds = unitVOs.stream().map(RiskNoticeCardVO::getAreaId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskAreaEntity> areaMap = CollectionUtils.isEmpty(areaIds) ? Map.of()
                : areaService.listByIds(areaIds).stream().collect(Collectors.toMap(
                RiskAreaEntity::getId, Function.identity(), (left, right) -> left));
        Set<String> departmentIds = new LinkedHashSet<>();
        areaMap.values().forEach(area -> RiskControlRefUtils.addId(departmentIds, area.getDepartmentId()));
        Map<String, String> departmentNames = ouaaFacadeService.batchDepartmentNames(departmentIds);
        unitVOs.forEach(vo -> {
            RiskAreaEntity area = areaMap.get(vo.getAreaId());
            if (area == null) {
                return;
            }
            vo.setArea(RiskUnitConvert.toOption(area));
            vo.setDepartmentId(area.getDepartmentId());
            vo.setDepartment(RiskControlRefUtils.toDepartment(area.getDepartmentId(), departmentNames));
        });
    }

    private void enrichNoticeCards(List<RiskNoticeCardVO> unitVOs) {
        if (CollectionUtils.isEmpty(unitVOs)) {
            return;
        }
        List<Long> unitIds = unitVOs.stream().map(RiskNoticeCardVO::getUnitId).filter(Objects::nonNull).toList();
        List<RiskUnitSignRelEntity> signRels = unitSignRelMapper.selectList(Wrappers.<RiskUnitSignRelEntity>lambdaQuery()
                .in(RiskUnitSignRelEntity::getUnitId, unitIds));
        List<Long> signIds = signRels.stream().map(RiskUnitSignRelEntity::getSignId).distinct().toList();
        Map<Long, RiskWarningSignEntity> signMap = CollectionUtils.isEmpty(signIds) ? Map.of()
                : warningSignMapper.selectBatchIds(signIds).stream().collect(Collectors.toMap(RiskWarningSignEntity::getId, Function.identity()));
        Map<Long, List<RiskWarningSignVO>> unitSignMap = signRels.stream().collect(Collectors.groupingBy(RiskUnitSignRelEntity::getUnitId,
                Collectors.mapping(rel -> {
                    RiskWarningSignEntity sign = signMap.get(rel.getSignId());
                    return sign == null ? new RiskWarningSignVO() : toWarningSignVO(sign);
                }, Collectors.toList())));
        List<RiskObjectEntity> objects = objectMapper.selectList(Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getUnitId, unitIds));
        List<Long> objectIds = objects.stream().map(RiskObjectEntity::getId).toList();
        Map<Long, List<RiskControlMeasureEntity>> measureMap = CollectionUtils.isEmpty(objectIds) ? Map.of()
                : measureMapper.selectList(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .in(RiskControlMeasureEntity::getObjectId, objectIds))
                .stream().collect(Collectors.groupingBy(RiskControlMeasureEntity::getObjectId));
        Map<Long, List<RiskObjectVO>> objectMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId,
                Collectors.mapping(object -> {
                    RiskObjectVO objectVO = RiskIdentificationConvert.toVO(object);
                    objectVO.setMeasures(measureMap.getOrDefault(object.getId(), List.of()).stream().map(RiskMeasureConvert::toVO).toList());
                    return objectVO;
                }, Collectors.toList())));
        unitVOs.forEach(vo -> {
            vo.setWarningSigns(unitSignMap.getOrDefault(vo.getUnitId(), List.of()));
            vo.setObjects(objectMap.getOrDefault(vo.getUnitId(), List.of()));
        });
        enrichNoticeCardRefs(unitVOs);
    }

    private void enrichNoticeCardRefs(List<RiskNoticeCardVO> unitVOs) {
        enrichNoticeCardDepartments(unitVOs);
        List<RiskWarningSignVO> signs = unitVOs.stream()
                .filter(unit -> CollectionUtils.isNotEmpty(unit.getWarningSigns()))
                .flatMap(unit -> unit.getWarningSigns().stream())
                .toList();
        enrichWarningSignIcons(signs);

        List<RiskObjectVO> objects = unitVOs.stream()
                .filter(unit -> CollectionUtils.isNotEmpty(unit.getObjects()))
                .flatMap(unit -> unit.getObjects().stream())
                .toList();
        enrichObjectAndMeasureUsers(objects);
    }

    private void enrichObjectAndMeasureUsers(List<RiskObjectVO> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return;
        }
        Set<String> userIds = new LinkedHashSet<>();
        objects.forEach(object -> {
            RiskControlRefUtils.addId(userIds, object.getResponsibleUserId());
            if (CollectionUtils.isNotEmpty(object.getMeasures())) {
                object.getMeasures().forEach(measure -> RiskControlRefUtils.addId(userIds, measure.getExecutorUserId()));
            }
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        objects.forEach(object -> {
            object.setResponsibleUser(RiskControlRefUtils.toUser(object.getResponsibleUserId(), userNames));
            if (CollectionUtils.isNotEmpty(object.getMeasures())) {
                object.getMeasures().forEach(measure -> measure.setExecutorUser(
                        RiskControlRefUtils.toUser(measure.getExecutorUserId(), userNames)));
            }
        });
    }

    private void enrichWarningSignIcons(List<RiskWarningSignVO> signs) {
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

    private String optionName(RiskOptionVO option) {
        return option == null ? "" : option.getName();
    }

    private String userName(RiskUserRefVO user) {
        return user == null ? "" : user.getUserName();
    }

    private String userNames(List<RiskUserRefVO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return "";
        }
        return users.stream().map(this::userName).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
    }

    private String warningSignName(RiskUnitInventoryVO vo) {
        return vo.getWarningSign() == null ? "" : vo.getWarningSign().getName();
    }

    private String departmentName(RiskDepartmentRefVO department) {
        return department == null ? "" : department.getDepartmentName();
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

    private record InventoryQueryCriteria(String keyword,
                                          Long unitId,
                                          Long areaId,
                                          Long unitTypeId,
                                          String riskLevel,
                                          String controlStatus,
                                          String departmentId) {
    }
}
