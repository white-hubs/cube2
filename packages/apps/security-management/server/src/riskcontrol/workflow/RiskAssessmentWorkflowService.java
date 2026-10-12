package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.convert.RiskAssessmentConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskAssessmentRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskChangeLogEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskAssessmentCalculator;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskAssessmentRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskChangeLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity.RiskAreaEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.service.impl.RiskAreaService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskAssessmentTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskLevelRank;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskStateMachine;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskUnitControlStatusResolver;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPatchParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskDataPermissionContext;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentDimensionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentLevelRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentMethodEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentOptionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentDimensionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentLevelRuleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentMethodService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentOptionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.convert.RiskWarningConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity.RiskWarningRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity.RiskWarningRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.service.impl.RiskWarningRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.service.impl.RiskWarningRuleService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("securityManagementRiskAssessmentWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskAssessmentWorkflowService {
    private final RiskObjectService objectService;
    private final RiskUnitService unitService;
    private final RiskAreaService areaService;
    private final RiskAssessmentRecordService assessmentRecordService;
    private final RiskChangeLogService changeLogService;
    private final RiskAssessmentMethodService methodService;
    private final RiskAssessmentDimensionService dimensionService;
    private final RiskAssessmentOptionService optionService;
    private final RiskAssessmentLevelRuleService levelRuleService;
    private final RiskAssessmentCalculator calculator;
    private final RiskWarningRuleService warningRuleService;
    private final RiskWarningRecordService warningRecordService;
    private final MessageFacadeService messageFacadeService;

    public Pagination<RiskObjectVO> assessmentTaskPage(RiskAssessmentTaskPageParam param) {
        List<Long> permittedUnitIds = permittedAssessmentUnitIds(param);
        if (CollectionUtils.isEmpty(permittedUnitIds)) {
            return Pagination.empty();
        }
        List<RiskObjectEntity> assessmentObjects = RiskDataPermissionContext.withoutDataPermission(() ->
                objectService.list(assessmentTaskObjectQuery(param, permittedUnitIds)));
        if (CollectionUtils.isEmpty(assessmentObjects)) {
            return Pagination.empty();
        }
        List<Long> taskUnitIds = assessmentObjects.stream()
                .map(RiskObjectEntity::getUnitId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<Long> filteredUnitIds = filterAssessmentTaskUnitIds(param, assessmentObjects, taskUnitIds);
        if (CollectionUtils.isEmpty(filteredUnitIds)) {
            return Pagination.empty();
        }
        Set<Long> filteredUnitIdSet = new LinkedHashSet<>(filteredUnitIds);
        Map<Long, List<RiskObjectEntity>> objectMap = assessmentObjects.stream()
                .filter(object -> object.getUnitId() != null)
                .filter(object -> filteredUnitIdSet.contains(object.getUnitId()))
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        if (StringUtils.isNotBlank(param.getStatus())) {
            objectMap = objectMap.entrySet().stream()
                    .filter(entry -> matchesAssessmentStatus(param.getStatus(), resolveAssessmentStatus(entry.getValue())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (MapUtils.isEmpty(objectMap)) {
            return Pagination.empty();
        }
        List<Long> pageUnitIds = objectMap.keySet().stream().toList();
        IPage<RiskUnitEntity> page = unitService.page(PagingUtils.convert(param), assessmentTaskUnitQuery(pageUnitIds, param));
        Pagination<RiskObjectVO> pagination = RiskPageAssembler.toPagination(page, RiskAssessmentWorkflowService::toAssessmentTaskVO);
        final Map<Long, List<RiskObjectEntity>> finalObjectMap = objectMap;
        pagination.getRecords().forEach(unit -> {
            unit.setUnitId(unit.getId());
            List<RiskObjectEntity> unitObjects = finalObjectMap.getOrDefault(unit.getId(), List.of());
            unit.setObjectCount((long) unitObjects.size());
            unit.setCompletedCount(unitObjects.stream()
                    .filter(object -> !Objects.equals(object.getStatus(), RiskObjectStatusEnum.RE_EVAL.name()))
                    .filter(object -> !Objects.equals(object.getStatus(), RiskObjectStatusEnum.INHERENT_EVAL.name()))
                    .filter(object -> !Objects.equals(object.getStatus(), RiskObjectStatusEnum.CONTROL_EVAL.name()))
                    .count());
            unit.setInherentRiskLevel(RiskLevelRank.highest(unitObjects.stream()
                    .map(RiskObjectEntity::getInherentRiskLevel)
                    .toList()));
            unit.setControlRiskLevel(RiskLevelRank.highest(unitObjects.stream()
                    .map(RiskObjectEntity::getControlRiskLevel)
                    .toList()));
            unit.setStatus(resolveAssessmentStatus(unitObjects));
        });
        return pagination;
    }

    private static RiskObjectVO toAssessmentTaskVO(RiskUnitEntity entity) {
        RiskObjectVO vo = new RiskObjectVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getUnitCode());
        vo.setName(entity.getUnitName());
        vo.setUnitId(entity.getId());
        vo.setAreaId(entity.getAreaId());
        vo.setUnitTypeId(entity.getUnitTypeId());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setControlStatus(entity.getControlStatus());
        vo.setInherentRiskLevel(entity.getInherentRiskLevel());
        vo.setControlRiskLevel(entity.getControlRiskLevel());
        return vo;
    }

    private LambdaQueryWrapper<RiskObjectEntity> assessmentTaskObjectQuery(RiskAssessmentTaskPageParam param,
                                                                           List<Long> permittedUnitIds) {
        return Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getStatus,
                    RiskObjectStatusEnum.RE_EVAL.name(),
                    RiskObjectStatusEnum.INHERENT_EVAL.name(),
                    RiskObjectStatusEnum.CONTROL_EVAL.name(),
                    RiskObjectStatusEnum.CONTROL_SET.name(),
                    RiskObjectStatusEnum.EXECUTING.name(),
                    RiskObjectStatusEnum.ACTIVE.name())
                .in(RiskObjectEntity::getUnitId, permittedUnitIds)
                .eq(param.getUnitId() != null, RiskObjectEntity::getUnitId, param.getUnitId())
                .and(StringUtils.isNotBlank(param.getRiskLevel()), wrapper -> wrapper
                        .eq(RiskObjectEntity::getControlRiskLevel, param.getRiskLevel())
                        .or()
                        .eq(RiskObjectEntity::getInherentRiskLevel, param.getRiskLevel()));
    }

    private List<Long> permittedAssessmentUnitIds(RiskAssessmentTaskPageParam param) {
        return unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                        .eq(param.getUnitId() != null, RiskUnitEntity::getId, param.getUnitId())
                        .eq(param.getAreaId() != null, RiskUnitEntity::getAreaId, param.getAreaId()))
                .stream()
                .map(RiskUnitEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<Long> filterAssessmentTaskUnitIds(RiskAssessmentTaskPageParam param,
                                                   List<RiskObjectEntity> taskObjects,
                                                   List<Long> taskUnitIds
                                                  ) {
        if (StringUtils.isBlank(param.getKeyword())) {
            return taskUnitIds;
        }
        String keyword = param.getKeyword().trim();
        Set<Long> matchedUnitIds = taskObjects.stream()
                .filter(object -> contains(object.getObjectName(), keyword) || contains(object.getHazardDesc(), keyword))
                .map(RiskObjectEntity::getUnitId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<Long> unitMatchedIds = unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                             .in(RiskUnitEntity::getId, taskUnitIds)
                                                             .and(wrapper -> wrapper
                                                                     .like(RiskUnitEntity::getUnitName, keyword)
                                                                     .or()
                                                                     .like(RiskUnitEntity::getLocationDesc, keyword)))
                .stream()
                .map(RiskUnitEntity::getId)
                .toList();
        matchedUnitIds.addAll(unitMatchedIds);
        return matchedUnitIds.stream().toList();
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.contains(keyword);
    }

    private LambdaQueryWrapper<RiskUnitEntity> assessmentTaskUnitQuery(List<Long> unitIds, RiskAssessmentTaskPageParam param) {
        return Wrappers.<RiskUnitEntity>lambdaQuery()
                .in(RiskUnitEntity::getId, unitIds)
                .eq(param.getAreaId() != null, RiskUnitEntity::getAreaId, param.getAreaId())
                .orderByDesc(RiskUnitEntity::getId);
    }

    private String resolveAssessmentStatus(List<RiskObjectEntity> unitObjects) {
        if (CollectionUtils.isEmpty(unitObjects)) {
            return "PENDING";
        }
        boolean allInherentPending = unitObjects.stream()
                .allMatch(object -> Objects.equals(object.getStatus(), RiskObjectStatusEnum.INHERENT_EVAL.name())
                        || Objects.equals(object.getStatus(), RiskObjectStatusEnum.RE_EVAL.name()));
        if (allInherentPending) {
            return "PENDING";
        }
        boolean hasPendingAssessment = unitObjects.stream().anyMatch(object ->
                                                                             Objects.equals(object.getStatus(), RiskObjectStatusEnum.INHERENT_EVAL.name())
                                                                             ||
                                                                             Objects.equals(object.getStatus(), RiskObjectStatusEnum.RE_EVAL.name())
                                                                             ||
                                                                             Objects.equals(object.getStatus(), RiskObjectStatusEnum.CONTROL_EVAL.name()));
        return hasPendingAssessment ? "ASSESSING" : "COMPLETED";
    }

    private boolean matchesAssessmentStatus(String queryStatus, String actualStatus) {
        String normalized = normalizeAssessmentStatus(queryStatus);
        return StringUtils.isBlank(normalized) || Objects.equals(normalized, actualStatus);
    }

    private String normalizeAssessmentStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return switch (status.trim().toUpperCase()) {
            case "PENDING", "WAITING", "TODO", "待评估" -> "PENDING";
            case "ASSESSING", "IN_PROGRESS", "PROCESSING", "评估中" -> "ASSESSING";
            case "COMPLETED", "DONE", "FINISHED", "已完成" -> "COMPLETED";
            default -> status.trim().toUpperCase();
        };
    }

    public List<RiskAssessmentRecordVO> objectAssessments(Long objectId, String assessmentType) {
        return assessmentRecordService.list(Wrappers.<RiskAssessmentRecordEntity>lambdaQuery()
                                                    .eq(RiskAssessmentRecordEntity::getObjectId, objectId)
                                                    .eq(StringUtils.isNotBlank(assessmentType), RiskAssessmentRecordEntity::getAssessmentType, assessmentType)
                                                    .orderByDesc(RiskAssessmentRecordEntity::getId))
                .stream().map(RiskAssessmentConvert::toVO).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentRecordVO createAssessment(RiskAssessmentParam param) {
        RiskObjectEntity object = objectService.getById(param.getObjectId());
        Assert.isNotFound(object == null, "风险对象不存在");
        RiskAssessmentTypeEnum type = parseAssessmentType(param.getAssessmentType());
        requireAssessStatus(object, type);
        normalizeAssessmentMethod(param, type);
        RiskAssessmentRecordEntity record = buildRecord(param, type);
        assessmentRecordService.save(record);
        if (Boolean.TRUE.equals(record.getSubmitted())) {
            applyAssessmentResult(object, record);
        }
        return RiskAssessmentConvert.toVO(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentRecordVO patchAssessment(Long id, RiskAssessmentPatchParam param) {
        RiskAssessmentRecordEntity record = assessmentRecordService.getById(id);
        Assert.isNotFound(record == null, "评估记录不存在");
        RiskObjectEntity object = objectService.getById(record.getObjectId());
        Assert.isNotFound(object == null, "风险对象不存在");
        RiskAssessmentTypeEnum type = RiskAssessmentTypeEnum.valueOf(record.getAssessmentType());
        requireAssessStatus(object, type);
        RiskAssessmentParam effectiveParam = toAssessmentParam(id, record, param);
        normalizeAssessmentMethod(effectiveParam, type);
        RiskAssessmentRecordEntity newRecord = buildRecord(effectiveParam, type);
        record.setMethodId(newRecord.getMethodId());
        record.setTotalScore(newRecord.getTotalScore());
        record.setRiskLevel(newRecord.getRiskLevel());
        record.setMethodSnapshotJson(newRecord.getMethodSnapshotJson());
        record.setScoreDetailJson(newRecord.getScoreDetailJson());
        record.setSubmitted(newRecord.getSubmitted());
        assessmentRecordService.updateById(record);
        if (Boolean.TRUE.equals(record.getSubmitted())) {
            applyAssessmentResult(object, record);
        }
        return RiskAssessmentConvert.toVO(record);
    }

    private RiskAssessmentParam toAssessmentParam(Long id,
                                                  RiskAssessmentRecordEntity record,
                                                  RiskAssessmentPatchParam param) {
        RiskAssessmentParam target = new RiskAssessmentParam();
        target.setId(id);
        target.setObjectId(record.getObjectId());
        target.setAssessmentType(record.getAssessmentType());
        target.setMethodId(param.getMethodId() == null ? record.getMethodId() : param.getMethodId());
        target.setDimensionScores(param.getDimensionScores());
        target.setTotalScore(param.getTotalScore());
        target.setScoreDetailJson(param.getScoreDetailJson());
        target.setSubmitted(param.getSubmitted());
        return target;
    }

    private RiskAssessmentTypeEnum parseAssessmentType(String assessmentType) {
        try {
            return RiskAssessmentTypeEnum.valueOf(assessmentType);
        } catch (IllegalArgumentException | NullPointerException e) {
            Assert.isParameterError(true, "评估类型非法");
            return null;
        }
    }

    private void normalizeAssessmentMethod(RiskAssessmentParam param, RiskAssessmentTypeEnum type) {
        if (type != RiskAssessmentTypeEnum.CONTROL) {
            return;
        }
        RiskAssessmentRecordEntity inherentRecord = assessmentRecordService.getOne(Wrappers.<RiskAssessmentRecordEntity>lambdaQuery()
                                                                                           .eq(RiskAssessmentRecordEntity::getObjectId, param.getObjectId())
                                                                                           .eq(RiskAssessmentRecordEntity::getAssessmentType, RiskAssessmentTypeEnum.INHERENT.name())
                                                                                           .eq(RiskAssessmentRecordEntity::getSubmitted, true)
                                                                                           .orderByDesc(RiskAssessmentRecordEntity::getId)
                                                                                           .last("limit 1"));
        Assert.isNotFound(inherentRecord == null, "固有评估记录不存在");
        Assert.isParameterError(param.getMethodId() != null && !Objects.equals(param.getMethodId(), inherentRecord.getMethodId()),
                                "控制评估必须沿用固有评估方法");
        param.setMethodId(inherentRecord.getMethodId());
    }

    private RiskAssessmentRecordEntity buildRecord(RiskAssessmentParam param, RiskAssessmentTypeEnum type) {
        RiskAssessmentMethodEntity method = methodService.getById(param.getMethodId());
        Assert.isNotFound(method == null, "评估方法不存在");
        List<RiskAssessmentDimensionEntity> dimensions = dimensionService.list(Wrappers.<RiskAssessmentDimensionEntity>lambdaQuery()
                                                                                       .eq(RiskAssessmentDimensionEntity::getMethodId, method.getId()));
        List<Long> dimensionIds = dimensions.stream().map(RiskAssessmentDimensionEntity::getId).toList();
        List<RiskAssessmentOptionEntity> options = CollectionUtils.isEmpty(dimensionIds) ? List.of()
                                                                                         : optionService.list(Wrappers
                                                                                                                      .<RiskAssessmentOptionEntity>lambdaQuery()
                                                                                                                      .in(RiskAssessmentOptionEntity::getDimensionId, dimensionIds));
        List<RiskAssessmentLevelRuleEntity> rules = levelRuleService.list(Wrappers.<RiskAssessmentLevelRuleEntity>lambdaQuery()
                                                                                  .eq(RiskAssessmentLevelRuleEntity::getMethodId, method.getId()));
        validateDimensionScores(method, dimensions, options, param.getDimensionScores());
        BigDecimal totalScore = calculator.calculate(method.getFormula(), param.getDimensionScores(), param.getTotalScore());
        String level = calculator.hitLevel(rules, totalScore);
        Assert.isNotAcceptable(level == null, "评估分值未命中风险等级");
        RiskAssessmentRecordEntity record = new RiskAssessmentRecordEntity();
        record.setId(param.getId() == null ? IdWorker.getId() : param.getId());
        record.setObjectId(param.getObjectId());
        record.setMethodId(method.getId());
        record.setAssessmentType(type.name());
        record.setTotalScore(totalScore);
        record.setRiskLevel(level);
        record.setMethodSnapshotJson(JsonUtils.serialize(Map.of("method", method, "dimensions", dimensions, "options", options, "rules", rules)));
        record.setScoreDetailJson(
                param.getScoreDetailJson() == null ? JsonUtils.serialize(param.getDimensionScores()) : param.getScoreDetailJson());
        record.setSubmitted(param.getSubmitted() == null || param.getSubmitted());
        return record;
    }

    private void validateDimensionScores(RiskAssessmentMethodEntity method,
                                         List<RiskAssessmentDimensionEntity> dimensions,
                                         List<RiskAssessmentOptionEntity> options,
                                         Map<String, BigDecimal> dimensionScores
                                        ) {
        if (method.getFormula() == null || method.getFormula().isBlank()) {
            return;
        }
        Assert.isParameterError(dimensionScores == null || dimensionScores.isEmpty(), "评估维度分值不能为空");
        Set<String> dimensionCodes = dimensions.stream().map(RiskAssessmentDimensionEntity::getDimensionCode).collect(Collectors.toSet());
        Assert.isParameterError(!dimensionCodes.containsAll(dimensionScores.keySet()), "评估维度编码非法");
        Map<Long, List<RiskAssessmentOptionEntity>> optionMap = options.stream()
                .collect(Collectors.groupingBy(RiskAssessmentOptionEntity::getDimensionId));
        Map<String, Long> dimensionIdMap = dimensions.stream()
                .collect(Collectors.toMap(RiskAssessmentDimensionEntity::getDimensionCode, RiskAssessmentDimensionEntity::getId));
        for (Map.Entry<String, BigDecimal> entry : dimensionScores.entrySet()) {
            Assert.isParameterError(entry.getValue() == null, "评估维度分值不能为空");
            List<RiskAssessmentOptionEntity> dimensionOptions = optionMap.getOrDefault(dimensionIdMap.get(entry.getKey()), List.of());
            if (CollectionUtils.isEmpty(dimensionOptions)) {
                continue;
            }
            boolean matched = dimensionOptions.stream()
                    .anyMatch(option -> option.getScore() != null && option.getScore().compareTo(entry.getValue()) == 0);
            Assert.isParameterError(!matched, "评估维度分值必须来自配置选项：" + entry.getKey());
        }
    }

    private void requireAssessStatus(RiskObjectEntity object, RiskAssessmentTypeEnum type) {
        if (type == RiskAssessmentTypeEnum.INHERENT) {
            RiskObjectStatusEnum actual = RiskObjectStatusEnum.valueOf(object.getStatus());
            Assert.isNotAcceptable(actual != RiskObjectStatusEnum.INHERENT_EVAL && actual != RiskObjectStatusEnum.RE_EVAL,
                                   "仅固有评估状态对象可以提交固有评估");
        } else {
            RiskStateMachine.require(RiskObjectStatusEnum.valueOf(object.getStatus()), RiskObjectStatusEnum.CONTROL_EVAL,
                                     "仅控制评估状态对象可以提交控制评估");
        }
    }

    private void applyAssessmentResult(RiskObjectEntity object, RiskAssessmentRecordEntity record) {
        String before = JsonUtils.serialize(object);
        String beforeLevel = Objects.equals(record.getAssessmentType(), RiskAssessmentTypeEnum.INHERENT.name())
                             ? object.getInherentRiskLevel() : object.getControlRiskLevel();
        if (Objects.equals(record.getAssessmentType(), RiskAssessmentTypeEnum.INHERENT.name())) {
            object.setInherentRiskLevel(record.getRiskLevel());
            object.setStatus(RiskObjectStatusEnum.CONTROL_EVAL.name());
        } else {
            object.setControlRiskLevel(record.getRiskLevel());
            object.setStatus(RiskObjectStatusEnum.CONTROL_SET.name());
        }
        objectService.updateById(object);
        RiskChangeLogEntity changeLog = new RiskChangeLogEntity();
        changeLog.setId(IdWorker.getId());
        changeLog.setBusinessType("RISK_OBJECT");
        changeLog.setBusinessId(object.getId());
        changeLog.setChangeType(record.getAssessmentType() + "_ASSESSMENT");
        changeLog.setBeforeJson(before);
        changeLog.setAfterJson(JsonUtils.serialize(object));
        changeLogService.save(changeLog);
        createLevelUpgradeWarningIfNecessary(object, beforeLevel, record.getRiskLevel());
        refreshUnitRiskLevel(List.of(object.getUnitId()));
    }

    private void createLevelUpgradeWarningIfNecessary(RiskObjectEntity object, String beforeLevel, String afterLevel) {
        if (!isLevelUpgrade(beforeLevel, afterLevel)) {
            return;
        }
        RiskWarningRuleEntity rule = ensureLevelUpgradeRule(afterLevel);
        if (rule == null) {
            return;
        }
        List<Long> levelUpgradeRuleIds = levelUpgradeRuleIds();
        long activeCount = warningRecordService.count(Wrappers.<RiskWarningRecordEntity>lambdaQuery()
                                                              .in(CollectionUtils.isNotEmpty(levelUpgradeRuleIds),
                                                                      RiskWarningRecordEntity::getRuleId, levelUpgradeRuleIds)
                                                              .eq(RiskWarningRecordEntity::getUnitId, object.getUnitId())
                                                              .eq(RiskWarningRecordEntity::getObjectId, object.getId())
                                                              .eq(RiskWarningRecordEntity::getWarningStatus, RiskWarningStatusEnum.ACTIVE.name()));
        if (activeCount > 0) {
            return;
        }
        RiskWarningRecordEntity warning = RiskWarningConvert.toRecordEntity(rule, object.getUnitId(), object.getId());
        warning.setId(IdWorker.getId());
        warning.setSeverity(warningSeverity(afterLevel, rule.getSeverity()));
        warning.setTriggerTime(LocalDateTime.now());
        warningRecordService.save(warning);
        messageFacadeService.sendLevelUpgradeWarning(object.getUnitId(), object.getId(), beforeLevel, afterLevel);
    }

    private RiskWarningRuleEntity ensureLevelUpgradeRule(String afterLevel) {
        RiskWarningRuleEntity rule = warningRuleService.getOne(Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                                                                       .eq(RiskWarningRuleEntity::getWarningType, RiskWarningTypeEnum.LEVEL_UPGRADE.name())
                                                                       .orderByDesc(RiskWarningRuleEntity::getId)
                                                                       .last("limit 1"));
        if (rule == null) {
            rule = new RiskWarningRuleEntity();
            rule.setId(IdWorker.getId());
            rule.setWarningType(RiskWarningTypeEnum.LEVEL_UPGRADE.name());
            rule.setConditionJson("{\"trigger\":\"RISK_OBJECT_LEVEL_UPGRADE\"}");
            rule.setSeverity(warningSeverity(afterLevel, null));
            rule.setDisposeDeadlineDays(7);
            rule.setEnabled(true);
            warningRuleService.save(rule);
            return rule;
        }
        if (!Boolean.TRUE.equals(rule.getEnabled())) {
            rule.setEnabled(true);
            warningRuleService.updateById(rule);
        }
        return rule;
    }

    private List<Long> levelUpgradeRuleIds() {
        return warningRuleService.list(Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                                      .eq(RiskWarningRuleEntity::getWarningType, RiskWarningTypeEnum.LEVEL_UPGRADE.name()))
                .stream()
                .map(RiskWarningRuleEntity::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    private String warningSeverity(String riskLevel, String fallback) {
        if (StringUtils.isNotBlank(riskLevel)) {
            try {
                return switch (RiskLevelEnum.valueOf(riskLevel)) {
                    case MAJOR -> "RED";
                    case LARGE -> "ORANGE";
                    case GENERAL, LOW -> "YELLOW";
                };
            } catch (IllegalArgumentException ignored) {
            }
        }
        return StringUtils.isNotBlank(fallback) ? fallback : "YELLOW";
    }

    private boolean isLevelUpgrade(String beforeLevel, String afterLevel) {
        if (beforeLevel == null || afterLevel == null) {
            return false;
        }
        try {
            return RiskLevelEnum.valueOf(afterLevel).getWeight() > RiskLevelEnum.valueOf(beforeLevel).getWeight();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void refreshUnitRiskLevel(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return;
        }
        List<RiskUnitEntity> units = unitService.listByIds(unitIds).stream().toList();
        if (CollectionUtils.isEmpty(units)) {
            return;
        }
        List<RiskObjectEntity> objects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                                                                    .in(RiskObjectEntity::getUnitId, unitIds));
        Map<Long, List<RiskObjectEntity>> objectMap = objects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        units.forEach(unit -> {
            List<RiskObjectEntity> unitObjects = objectMap.getOrDefault(unit.getId(), List.of());
            unit.setInherentRiskLevel(RiskLevelRank.highest(unitObjects.stream().map(RiskObjectEntity::getInherentRiskLevel).toList()));
            unit.setControlRiskLevel(RiskLevelRank.highest(unitObjects.stream().map(RiskObjectEntity::getControlRiskLevel).toList()));
            unit.setRiskLevel(RiskLevelRank.highest(unitObjects.stream().map(RiskAssessmentWorkflowService::currentObjectRiskLevel).toList()));
            unit.setControlStatus(RiskUnitControlStatusResolver.resolve(unitObjects));
        });
        unitService.updateBatchById(units);
        refreshAreaRiskLevel(units.stream()
                                     .map(RiskUnitEntity::getAreaId)
                                     .filter(Objects::nonNull)
                                     .distinct()
                                     .toList());
    }

    private void refreshAreaRiskLevel(List<Long> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            return;
        }
        List<RiskAreaEntity> areas = areaService.listByIds(areaIds).stream().toList();
        if (CollectionUtils.isEmpty(areas)) {
            return;
        }
        List<RiskUnitEntity> units = unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                                                              .in(RiskUnitEntity::getAreaId, areaIds));
        Map<Long, List<RiskUnitEntity>> unitMap = units.stream().collect(Collectors.groupingBy(RiskUnitEntity::getAreaId));
        areas.forEach(area -> {
            List<RiskUnitEntity> areaUnits = unitMap.getOrDefault(area.getId(), List.of());
            String inherentRiskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskUnitEntity::getInherentRiskLevel).toList());
            String controlRiskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskUnitEntity::getControlRiskLevel).toList());
            String riskLevel = RiskLevelRank.highest(areaUnits.stream().map(RiskAssessmentWorkflowService::currentUnitRiskLevel).toList());
            areaService.update(Wrappers.<RiskAreaEntity>lambdaUpdate()
                                       .eq(RiskAreaEntity::getId, area.getId())
                                       .set(RiskAreaEntity::getInherentRiskLevel, inherentRiskLevel)
                                       .set(RiskAreaEntity::getControlRiskLevel, controlRiskLevel)
                                       .set(RiskAreaEntity::getRiskLevel, riskLevel));
        });
    }

    private static String currentObjectRiskLevel(RiskObjectEntity object) {
        if (object == null) {
            return null;
        }
        return StringUtils.isNotBlank(object.getControlRiskLevel()) ? object.getControlRiskLevel() : object.getInherentRiskLevel();
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
