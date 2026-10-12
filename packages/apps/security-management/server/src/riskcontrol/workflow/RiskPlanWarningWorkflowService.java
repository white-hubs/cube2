package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPlanPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskEnabledParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningDisposeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRecordPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRulePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentPlanVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUserRefVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskAssessmentRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskAssessmentRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskPlanStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskUnitIdentifyStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskUnitControlStatusResolver;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskExcelExportUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.convert.RiskPlanConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskAssessmentPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskPlanNotifyUserEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.service.impl.RiskAssessmentPlanService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.service.impl.RiskPlanNotifyUserService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
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
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("securityManagementRiskPlanWarningWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskPlanWarningWorkflowService {
    private static final int MYSQL_DATETIME_MIN_YEAR = 1000;
    private static final int MYSQL_DATETIME_MAX_YEAR = 9999;

    private final RiskAssessmentPlanService planService;
    private final RiskPlanNotifyUserService notifyUserService;
    private final RiskWarningRuleService warningRuleService;
    private final RiskWarningRecordService warningRecordService;
    private final RiskUnitService unitService;
    private final RiskObjectService objectService;
    private final RiskAssessmentRecordService assessmentRecordService;
    private final OuaaFacadeService ouaaFacadeService;
    private final MessageFacadeService messageFacadeService;

    public Pagination<RiskAssessmentPlanVO> planPage(RiskAssessmentPlanPageParam param) {
        List<Long> notifyPlanIds = planIdsByNotifyUser(param.getNotifyUserId());
        if (StringUtils.isNotBlank(param.getNotifyUserId()) && CollectionUtils.isEmpty(notifyPlanIds)) {
            return Pagination.empty();
        }
        IPage<RiskAssessmentPlanEntity> page = planService.page(PagingUtils.convert(param),
                Wrappers.<RiskAssessmentPlanEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskAssessmentPlanEntity::getPlanName, param.getKeyword())
                        .eq(param.getUnitId() != null, RiskAssessmentPlanEntity::getUnitId, param.getUnitId())
                        .eq(StringUtils.isNotBlank(param.getStatus()), RiskAssessmentPlanEntity::getStatus, param.getStatus())
                        .eq(StringUtils.isNotBlank(param.getCycleType()), RiskAssessmentPlanEntity::getCycleType, param.getCycleType())
                        .in(CollectionUtils.isNotEmpty(notifyPlanIds), RiskAssessmentPlanEntity::getId, notifyPlanIds)
                        .orderByAsc(RiskAssessmentPlanEntity::getNextExecuteTime));
        Pagination<RiskAssessmentPlanVO> pagination = RiskPageAssembler.toPagination(page, RiskPlanConvert::toVO);
        enrichPlanNotifyUsers(pagination.getRecords());
        return pagination;
    }

    private List<Long> planIdsByNotifyUser(String notifyUserId) {
        if (StringUtils.isBlank(notifyUserId)) {
            return List.of();
        }
        return notifyUserService.list(Wrappers.<RiskPlanNotifyUserEntity>lambdaQuery()
                        .eq(RiskPlanNotifyUserEntity::getUserId, notifyUserId.trim()))
                .stream()
                .map(RiskPlanNotifyUserEntity::getPlanId)
                .distinct()
                .toList();
    }

    public RiskAssessmentPlanVO planDetail(Long id) {
        RiskAssessmentPlanEntity entity = planService.getById(id);
        Assert.isNotFound(entity == null, "评估计划不存在");
        RiskAssessmentPlanVO vo = RiskPlanConvert.toVO(entity);
        enrichPlanNotifyUsers(List.of(vo));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentPlanVO createPlan(RiskAssessmentPlanParam param) {
        validatePlanParam(param);
        RiskAssessmentPlanEntity entity = RiskPlanConvert.toEntity(param);
        entity.setId(IdWorker.getId());
        planService.save(entity);
        saveNotifyUsers(entity.getId(), param);
        return planDetail(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentPlanVO updatePlan(Long id, RiskAssessmentPlanParam param) {
        RiskAssessmentPlanEntity entity = planService.getById(id);
        Assert.isNotFound(entity == null, "评估计划不存在");
        validatePlanParam(param);
        RiskPlanConvert.updateEntity(entity, param);
        planService.updateById(entity);
        replaceNotifyUsers(id, param);
        return planDetail(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentPlanVO patchPlan(Long id, RiskEnabledParam param) {
        RiskAssessmentPlanEntity entity = planService.getById(id);
        Assert.isNotFound(entity == null, "评估计划不存在");
        Assert.isParameterError(param.getEnabled() == null, "启用状态不能为空");
        entity.setStatus(Boolean.FALSE.equals(param.getEnabled()) ? RiskPlanStatusEnum.DISABLED.name() : RiskPlanStatusEnum.ENABLED.name());
        planService.updateById(entity);
        return planDetail(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        RiskAssessmentPlanEntity entity = planService.getById(id);
        Assert.isNotFound(entity == null, "评估计划不存在");
        List<RiskPlanNotifyUserEntity> users = notifyUserService.list(Wrappers.<RiskPlanNotifyUserEntity>lambdaQuery()
                .eq(RiskPlanNotifyUserEntity::getPlanId, id));
        if (CollectionUtils.isNotEmpty(users)) {
            DatabaseContextHolder.softDelete(notifyUserService, users);
        }
        DatabaseContextHolder.softDelete(planService, id);
    }

    public Pagination<RiskWarningRuleVO> warningRulePage(RiskWarningRulePageParam param) {
        IPage<RiskWarningRuleEntity> page = warningRuleService.page(PagingUtils.convert(param),
                Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                        .eq(param.getEnabled() != null, RiskWarningRuleEntity::getEnabled, param.getEnabled())
                        .orderByDesc(RiskWarningRuleEntity::getId));
        return RiskPageAssembler.toPagination(page, RiskWarningConvert::toVO);
    }

    public RiskWarningRuleVO warningRuleDetail(Long id) {
        RiskWarningRuleEntity entity = warningRuleService.getById(id);
        Assert.isNotFound(entity == null, "预警规则不存在");
        return RiskWarningConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningRuleVO createWarningRule(RiskWarningRuleParam param) {
        RiskWarningRuleEntity entity = RiskWarningConvert.toRuleEntity(param);
        entity.setId(IdWorker.getId());
        warningRuleService.save(entity);
        return RiskWarningConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningRuleVO updateWarningRule(Long id, RiskWarningRuleParam param) {
        RiskWarningRuleEntity entity = warningRuleService.getById(id);
        Assert.isNotFound(entity == null, "预警规则不存在");
        Assert.isConflict(param.getWarningType() != null && !Objects.equals(entity.getWarningType(), param.getWarningType()),
                "预警类型不可修改");
        param.setWarningType(entity.getWarningType());
        if (param.getEnabled() == null) {
            param.setEnabled(entity.getEnabled());
        }
        RiskWarningConvert.updateRuleEntity(entity, param);
        warningRuleService.updateById(entity);
        return RiskWarningConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningRuleVO patchWarningRule(Long id, RiskEnabledParam param) {
        RiskWarningRuleEntity entity = warningRuleService.getById(id);
        Assert.isNotFound(entity == null, "预警规则不存在");
        Assert.isParameterError(param.getEnabled() == null, "启用状态不能为空");
        entity.setEnabled(param.getEnabled());
        warningRuleService.updateById(entity);
        return RiskWarningConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWarningRule(Long id) {
        DatabaseContextHolder.softDelete(warningRuleService, id);
    }

    public Pagination<RiskWarningRecordVO> warningRecordPage(RiskWarningRecordPageParam param) {
        List<Long> ruleIds = ruleIdsByWarningType(param.getWarningType());
        if (StringUtils.isNotBlank(param.getWarningType()) && CollectionUtils.isEmpty(ruleIds)) {
            return Pagination.empty();
        }
        IPage<RiskWarningRecordEntity> page = warningRecordService.page(PagingUtils.convert(param),
                warningRecordQuery(param, ruleIds));
        Pagination<RiskWarningRecordVO> pagination = RiskPageAssembler.toPagination(page, RiskWarningConvert::toRecordVO);
        enrichWarningRecords(pagination.getRecords());
        return pagination;
    }

    public void exportWarningRecords(RiskWarningRecordPageParam param, HttpServletResponse response) {
        List<Long> ruleIds = ruleIdsByWarningType(param.getWarningType());
        List<RiskWarningRecordVO> records = StringUtils.isNotBlank(param.getWarningType()) && CollectionUtils.isEmpty(ruleIds) ? List.of()
                : warningRecordService.list(warningRecordQuery(param, ruleIds))
                .stream().map(RiskWarningConvert::toRecordVO).toList();
        enrichWarningRecords(records);
        List<Map<String, Object>> rows = records.stream().map(record -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("预警记录ID", record.getId());
            row.put("预警规则ID", record.getRuleId());
            row.put("预警类型", record.getWarningType());
            row.put("风险单元", record.getUnit() == null ? record.getUnitId() : record.getUnit().getName());
            row.put("风险对象", record.getObjectName() == null ? record.getObjectId() : record.getObjectName());
            row.put("严重程度", record.getSeverity());
            row.put("预警状态", record.getWarningStatus());
            row.put("触发时间", record.getTriggerTime());
            row.put("处置时间", record.getDisposeTime());
            return row;
        }).toList();
        RiskExcelExportUtils.write(response, "风险预警记录", rows);
    }

    private LambdaQueryWrapper<RiskWarningRecordEntity> warningRecordQuery(RiskWarningRecordPageParam param, List<Long> ruleIds) {
        return Wrappers.<RiskWarningRecordEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getStatus()), RiskWarningRecordEntity::getWarningStatus, param.getStatus())
                .in(CollectionUtils.isNotEmpty(ruleIds), RiskWarningRecordEntity::getRuleId, ruleIds)
                .eq(StringUtils.isNotBlank(param.getSeverity()), RiskWarningRecordEntity::getSeverity, param.getSeverity())
                .eq(param.getUnitId() != null, RiskWarningRecordEntity::getUnitId, param.getUnitId())
                .eq(param.getObjectId() != null, RiskWarningRecordEntity::getObjectId, param.getObjectId())
                .ge(param.getStartTime() != null, RiskWarningRecordEntity::getTriggerTime, param.getStartTime())
                .le(param.getEndTime() != null, RiskWarningRecordEntity::getTriggerTime, param.getEndTime())
                .orderByDesc(RiskWarningRecordEntity::getTriggerTime);
    }

    private List<Long> ruleIdsByWarningType(String warningType) {
        if (StringUtils.isBlank(warningType)) {
            return List.of();
        }
        return warningRuleService.list(Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                        .eq(RiskWarningRuleEntity::getWarningType, warningType.trim()))
                .stream()
                .map(RiskWarningRuleEntity::getId)
                .toList();
    }

    public RiskWarningRecordVO warningRecordDetail(Long id) {
        RiskWarningRecordEntity entity = warningRecordService.getById(id);
        Assert.isNotFound(entity == null, "预警记录不存在");
        RiskWarningRecordVO vo = RiskWarningConvert.toRecordVO(entity);
        enrichWarningRecords(List.of(vo));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningRecordVO disposeWarningRecord(Long id, RiskWarningDisposeParam param) {
        RiskWarningRecordEntity entity = warningRecordService.getById(id);
        Assert.isNotFound(entity == null, "预警记录不存在");
        if (param != null) {
            Assert.isParameterError(StringUtils.isNotBlank(param.getDisposeDesc()) && param.getDisposeDesc().length() > 500,
                    "处置说明不能超过500个字符");
            Assert.isParameterError(StringUtils.isNotBlank(param.getDisposeResult()) && param.getDisposeResult().length() > 50,
                    "处置结果不能超过50个字符");
            entity.setDisposeDesc(StringUtils.isBlank(param.getDisposeDesc()) ? null : param.getDisposeDesc().trim());
            entity.setDisposeResult(StringUtils.isBlank(param.getDisposeResult()) ? null : param.getDisposeResult().trim());
        }
        entity.setWarningStatus(RiskWarningStatusEnum.DISPOSED.name());
        entity.setDisposeTime(LocalDateTime.now());
        warningRecordService.updateById(entity);
        RiskWarningRecordVO vo = RiskWarningConvert.toRecordVO(entity);
        enrichWarningRecords(List.of(vo));
        return vo;
    }

    private void enrichWarningRecords(List<RiskWarningRecordVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        List<Long> ruleIds = records.stream().map(RiskWarningRecordVO::getRuleId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskWarningRuleEntity> ruleMap = CollectionUtils.isEmpty(ruleIds) ? Map.of()
                : warningRuleService.listByIds(ruleIds).stream().collect(Collectors.toMap(
                RiskWarningRuleEntity::getId, Function.identity(), (left, right) -> left));
        List<Long> unitIds = records.stream().map(RiskWarningRecordVO::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        List<Long> objectIds = records.stream().map(RiskWarningRecordVO::getObjectId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskObjectEntity> objectMap = CollectionUtils.isEmpty(objectIds) ? Map.of()
                : objectService.listByIds(objectIds).stream().collect(Collectors.toMap(
                RiskObjectEntity::getId, Function.identity(), (left, right) -> left));
        records.forEach(record -> {
            RiskWarningRuleEntity rule = ruleMap.get(record.getRuleId());
            if (rule != null) {
                record.setWarningType(rule.getWarningType());
            }
            RiskUnitEntity unit = unitMap.get(record.getUnitId());
            if (unit != null) {
                record.setUnit(RiskUnitConvert.toOption(unit));
            }
            RiskObjectEntity object = objectMap.get(record.getObjectId());
            if (object != null) {
                record.setObjectCode(object.getObjectCode());
                record.setObjectName(object.getObjectName());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void executeIdentifyCycleJob() {
        LocalDateTime now = LocalDateTime.now();
        List<RiskUnitEntity> dueUnits = unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                .eq(RiskUnitEntity::getIdentifyStatus, RiskUnitIdentifyStatusEnum.REVIEWED.name())
                .le(RiskUnitEntity::getNextIdentifyTime, now));
        dueUnits.forEach(unit -> {
            unit.setCurrentIdentifyRoundNo("RID-" + IdWorker.getId());
            unit.setIdentifyStatus(RiskUnitIdentifyStatusEnum.IDENTIFYING.name());
        });
        if (CollectionUtils.isNotEmpty(dueUnits)) {
            unitService.updateBatchById(dueUnits);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void executeAssessmentPlanJob() {
        LocalDateTime now = LocalDateTime.now();
        List<RiskAssessmentPlanEntity> enabledPlans = planService.list(Wrappers.<RiskAssessmentPlanEntity>lambdaQuery()
                .eq(RiskAssessmentPlanEntity::getStatus, RiskPlanStatusEnum.ENABLED.name()));
        if (CollectionUtils.isEmpty(enabledPlans)) {
            return;
        }
        RiskWarningRuleEntity overdueRule = warningRuleService.getOne(Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                .eq(RiskWarningRuleEntity::getWarningType, RiskWarningTypeEnum.ASSESSMENT_OVERDUE.name())
                .eq(RiskWarningRuleEntity::getEnabled, true)
                .last("limit 1"));
        List<RiskAssessmentPlanEntity> duePlans = enabledPlans.stream().filter(plan -> isPlanDue(plan, now)).toList();
        triggerDueAssessmentPlans(duePlans, now);

        int overdueDays = resolveOverdueDays(overdueRule);
        List<Long> unitIds = enabledPlans.stream().map(RiskAssessmentPlanEntity::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(RiskUnitEntity::getId, Function.identity()));
        enabledPlans.stream()
                .filter(plan -> isAssessmentOverdue(plan, unitMap.get(plan.getUnitId()), overdueDays, now))
                .forEach(plan -> createAssessmentOverdueWarningIfAbsent(overdueRule, plan.getUnitId()));

        if (CollectionUtils.isNotEmpty(duePlans)) {
            planService.updateBatchById(duePlans);
        }
    }

    private void triggerDueAssessmentPlans(List<RiskAssessmentPlanEntity> duePlans, LocalDateTime now) {
        if (CollectionUtils.isEmpty(duePlans)) {
            return;
        }
        List<Long> dueUnitIds = duePlans.stream()
                .map(RiskAssessmentPlanEntity::getUnitId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (CollectionUtils.isNotEmpty(dueUnitIds)) {
            List<RiskObjectEntity> assessmentObjects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                    .in(RiskObjectEntity::getUnitId, dueUnitIds)
                    .in(RiskObjectEntity::getStatus,
                            RiskObjectStatusEnum.ACTIVE.name(),
                            RiskObjectStatusEnum.RE_EVAL.name(),
                            RiskObjectStatusEnum.INHERENT_EVAL.name(),
                            RiskObjectStatusEnum.CONTROL_EVAL.name()));
            softDeleteUnsubmittedAssessmentRecords(assessmentObjects);
            assessmentObjects.forEach(object -> object.setStatus(RiskObjectStatusEnum.INHERENT_EVAL.name()));
            if (CollectionUtils.isNotEmpty(assessmentObjects)) {
                objectService.updateBatchById(assessmentObjects);
            }
            refreshUnitControlStatus(dueUnitIds);
        }
        duePlans.forEach(plan -> {
            messageFacadeService.sendAssessmentPlanDue(plan);
            plan.setLastExecuteTime(plan.getNextExecuteTime() == null ? now : plan.getNextExecuteTime());
            plan.setNextExecuteTime(nextExecuteTime(plan, now));
        });
    }

    private void softDeleteUnsubmittedAssessmentRecords(List<RiskObjectEntity> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return;
        }
        List<Long> objectIds = objects.stream().map(RiskObjectEntity::getId).filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(objectIds)) {
            return;
        }
        List<RiskAssessmentRecordEntity> records = assessmentRecordService.list(Wrappers.<RiskAssessmentRecordEntity>lambdaQuery()
                .in(RiskAssessmentRecordEntity::getObjectId, objectIds)
                .eq(RiskAssessmentRecordEntity::getSubmitted, false));
        if (CollectionUtils.isNotEmpty(records)) {
            DatabaseContextHolder.softDelete(assessmentRecordService, records);
        }
    }

    private boolean isPlanDue(RiskAssessmentPlanEntity plan, LocalDateTime now) {
        return plan.getNextExecuteTime() != null && !plan.getNextExecuteTime().isAfter(now);
    }

    private boolean isAssessmentOverdue(RiskAssessmentPlanEntity plan, RiskUnitEntity unit, int overdueDays, LocalDateTime now) {
        if (plan.getLastExecuteTime() == null || unit == null) {
            return false;
        }
        if (RiskUnitIdentifyStatusEnum.REVIEWED.name().equals(unit.getIdentifyStatus())) {
            return false;
        }
        return !plan.getLastExecuteTime().plusDays(overdueDays).isAfter(now);
    }

    private int resolveOverdueDays(RiskWarningRuleEntity rule) {
        if (rule == null || rule.getConditionJson() == null || rule.getConditionJson().isBlank()) {
            return 0;
        }
        try {
            Map<String, Object> condition = JsonUtils.deserialize(rule.getConditionJson(), new TypeReference<Map<String, Object>>() {});
            Object value = condition == null ? null : condition.get("overdueDays");
            if (value instanceof Number number) {
                return Math.max(number.intValue(), 0);
            }
            return value == null ? 0 : Math.max(Integer.parseInt(String.valueOf(value)), 0);
        } catch (RuntimeException e) {
            return 0;
        }
    }

    private void createAssessmentOverdueWarningIfAbsent(RiskWarningRuleEntity rule, Long unitId) {
        if (rule == null) {
            return;
        }
        long activeCount = warningRecordService.count(Wrappers.<RiskWarningRecordEntity>lambdaQuery()
                .eq(RiskWarningRecordEntity::getRuleId, rule.getId())
                .eq(RiskWarningRecordEntity::getUnitId, unitId)
                .isNull(RiskWarningRecordEntity::getObjectId)
                .eq(RiskWarningRecordEntity::getWarningStatus, RiskWarningStatusEnum.ACTIVE.name()));
        if (activeCount > 0) {
            return;
        }
        RiskWarningRecordEntity record = RiskWarningConvert.toRecordEntity(rule, unitId, null);
        record.setId(IdWorker.getId());
        record.setTriggerTime(LocalDateTime.now());
        warningRecordService.save(record);
    }

    private void refreshUnitControlStatus(List<Long> unitIds) {
        List<Long> ids = unitIds.stream().filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<RiskUnitEntity> units = unitService.listByIds(ids).stream().toList();
        List<RiskObjectEntity> objects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getUnitId, ids));
        Map<Long, List<RiskObjectEntity>> objectMap = objects.stream()
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        units.forEach(unit -> unit.setControlStatus(
                RiskUnitControlStatusResolver.resolve(objectMap.getOrDefault(unit.getId(), List.of()))));
        unitService.updateBatchById(units);
    }

    private LocalDateTime nextExecuteTime(RiskAssessmentPlanEntity plan, LocalDateTime now) {
        LocalDateTime base = plan.getNextExecuteTime() == null || plan.getNextExecuteTime().isBefore(now)
                ? now : plan.getNextExecuteTime();
        String cycleType = plan.getCycleType() == null ? "" : plan.getCycleType().toUpperCase(Locale.ROOT);
        return switch (cycleType) {
            case "DAY", "DAILY" -> base.plusDays(1);
            case "WEEK", "WEEKLY" -> base.plusWeeks(1);
            case "QUARTER", "QUARTERLY" -> base.plusMonths(3);
            case "HALF_YEAR", "HALF_YEARLY" -> base.plusMonths(6);
            case "YEAR", "YEARLY" -> base.plusYears(1);
            default -> base.plusMonths(1);
        };
    }

    private void validatePlanParam(RiskAssessmentPlanParam param) {
        Assert.isParameterError(param.getUnitId() == null, "评估单元不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getPlanName()), "计划名称不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getCycleType()), "评估周期不能为空");
        Assert.isParameterError(param.getNextExecuteTime() == null, "下次执行时间不能为空");
        Assert.isParameterError(param.getNextExecuteTime().getYear() < MYSQL_DATETIME_MIN_YEAR
                || param.getNextExecuteTime().getYear() > MYSQL_DATETIME_MAX_YEAR, "下次执行时间格式非法");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getNotifyUserIds()), "通知人员不能为空");
        RiskUnitEntity unit = unitService.getById(param.getUnitId());
        Assert.isNotFound(unit == null, "评估单元不存在");
        param.setPlanName(param.getPlanName().trim());
        param.setCycleType(normalizeCycleType(param.getCycleType()));
        param.setNotifyUserIds(param.getNotifyUserIds().stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .toList());
        Assert.isParameterError(CollectionUtils.isEmpty(param.getNotifyUserIds()), "通知人员不能为空");
    }

    private String normalizeCycleType(String cycleType) {
        String normalized = cycleType.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "DAY", "DAILY", "每日" -> "DAY";
            case "WEEK", "WEEKLY", "每周" -> "WEEK";
            case "MONTH", "MONTHLY", "月度", "每月" -> "MONTH";
            case "QUARTER", "QUARTERLY", "季度", "每季度" -> "QUARTER";
            case "HALF_YEAR", "HALF_YEARLY", "半年", "每半年" -> "HALF_YEAR";
            case "YEAR", "YEARLY", "年度", "每年" -> "YEAR";
            default -> {
                Assert.isParameterError(true, "评估周期非法");
                yield normalized;
            }
        };
    }

    private void saveNotifyUsers(Long planId, RiskAssessmentPlanParam param) {
        if (CollectionUtils.isNotEmpty(param.getNotifyUserIds())) {
            notifyUserService.saveBatch(param.getNotifyUserIds().stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(userId -> RiskPlanConvert.toNotifyUser(planId, userId))
                    .peek(entity -> entity.setId(IdWorker.getId()))
                    .toList());
        }
    }

    private void enrichPlanNotifyUsers(List<RiskAssessmentPlanVO> plans) {
        if (CollectionUtils.isEmpty(plans)) {
            return;
        }
        List<Long> planIds = plans.stream().map(RiskAssessmentPlanVO::getId).filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(planIds)) {
            return;
        }
        List<Long> unitIds = plans.stream().map(RiskAssessmentPlanVO::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        Map<Long, List<String>> notifyUserMap = notifyUserService.list(Wrappers.<RiskPlanNotifyUserEntity>lambdaQuery()
                        .in(RiskPlanNotifyUserEntity::getPlanId, planIds))
                .stream()
                .collect(Collectors.groupingBy(RiskPlanNotifyUserEntity::getPlanId,
                        Collectors.mapping(RiskPlanNotifyUserEntity::getUserId, Collectors.toList())));
        Set<String> userIds = new LinkedHashSet<>();
        notifyUserMap.values().forEach(ids -> RiskControlRefUtils.addIds(userIds, ids));
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        plans.forEach(plan -> {
            RiskUnitEntity unit = unitMap.get(plan.getUnitId());
            if (unit != null) {
                plan.setUnit(RiskUnitConvert.toOption(unit));
            }
            List<String> notifyUserIds = notifyUserMap.getOrDefault(plan.getId(), List.of());
            plan.setNotifyUserIds(notifyUserIds);
            plan.setNotifyUsers(toUserRefs(notifyUserIds, userNames));
        });
    }

    private void replaceNotifyUsers(Long planId, RiskAssessmentPlanParam param) {
        List<RiskPlanNotifyUserEntity> users = notifyUserService.list(Wrappers.<RiskPlanNotifyUserEntity>lambdaQuery()
                .eq(RiskPlanNotifyUserEntity::getPlanId, planId));
        if (CollectionUtils.isNotEmpty(users)) {
            DatabaseContextHolder.softDelete(notifyUserService, users);
        }
        saveNotifyUsers(planId, param);
    }

    private List<RiskUserRefVO> toUserRefs(List<String> userIds, Map<String, String> userNames) {
        if (userIds == null) {
            return null;
        }
        return userIds.stream()
                .filter(StringUtils::isNotBlank)
                .map(userId -> {
                    String normalizedUserId = userId.trim();
                    RiskUserRefVO user = new RiskUserRefVO();
                    user.setUserId(normalizedUserId);
                    user.setUserName(userNames.get(normalizedUserId));
                    return user;
                })
                .toList();
    }
}
