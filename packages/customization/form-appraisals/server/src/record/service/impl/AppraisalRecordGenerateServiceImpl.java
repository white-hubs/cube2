package com.guhecloud.cube.srv.gen.appraisals.server.record.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalDepartmentUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalPeriodUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormFlowGroupIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.OrganizationIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.dto.FormGroupPathSnapshot;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordCalculateResult;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordCalculateService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordDetailCalculateResult;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordDetailMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecord;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecordDetail;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.AppraisalRecordGenerateService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 考核记录生成服务实现类
 * 负责定时生成考核记录，包括数据采集、扣分匹配、分数计算等
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppraisalRecordGenerateServiceImpl implements AppraisalRecordGenerateService {
    private final FormAppraisalRuleMapper ruleMapper;
    private final FormAppraisalRecordMapper recordMapper;
    private final FormAppraisalRecordDetailMapper detailMapper;
    private final FormFlowGroupIntegrationService formFlowGroupIntegrationService;
    private final OrganizationIntegrationService organizationIntegrationService;
    private final AppraisalRecordCalculateService calculateService;

    /**
     * 根据周期类型自动生成考核记录
     * 基于当前时间判断是否应该生成上一周期的记录
     *
     * @param periodType   周期类型
     * @param finalNowTime 当前时间
     */
    @Override
    public void generateByPeriod(AppraisalPeriodTypeEnum periodType, LocalDateTime finalNowTime) {
        // 判断是否应该生成该周期的记录，不应该则直接返回
        if (!AppraisalPeriodUtils.shouldGenerate(periodType, finalNowTime)) {
            return;
        }
        // 计算上一周期的年度和期数
        AppraisalPeriodUtils.TargetPeriod targetPeriod = AppraisalPeriodUtils.previousPeriod(periodType, finalNowTime);
        // 调用指定周期的生成方法
        generateByTargetPeriod(periodType, targetPeriod.getAssessmentYear(), targetPeriod.getAssessmentPeriod());
    }

    /**
     * 根据指定的年度和期数生成考核记录
     * 为所有启用的规则生成记录
     *
     * @param periodType       周期类型
     * @param assessmentYear   考核年度
     * @param assessmentPeriod 考核期数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateByTargetPeriod(AppraisalPeriodTypeEnum periodType, Integer assessmentYear, Integer assessmentPeriod) {
        // 查询该周期类型下所有启用状态的规则
        List<FormAppraisalRule> rules = ruleMapper.selectList(Wrappers.<FormAppraisalRule>lambdaQuery()
                                                                      .eq(FormAppraisalRule::getPeriodType, periodType)
                                                                      .eq(FormAppraisalRule::getEnabled, true));
        Map<Long, FormGroupPathSnapshot> groupSnapshotMap = getGroupSnapshotMapIfNeeded(rules);
        // 遍历每个规则，为其生成考核记录
        for (FormAppraisalRule rule : rules) {
            generateSingleRuleRecord(rule, assessmentYear, assessmentPeriod, groupSnapshotMap);
        }
    }

    /**
     * 在允许的条件下重新生成记录
     * 仅当记录处于生成状态时才允许重新生成
     *
     * @param recordId 记录 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regenerateIfAllowed(Long recordId) {
        // 查询现有的考核记录
        FormAppraisalRecord existing = recordMapper.selectById(recordId);
        // 如果记录不存在或不是生成状态，则不允许重新生成
        if (existing == null || existing.getStatus() != AppraisalRecordStatusEnum.GENERATED) {
            return;
        }
        // 查询关联的考核规则
        FormAppraisalRule rule = ruleMapper.selectById(existing.getRuleId());
        // 如果规则不存在，直接返回
        if (rule == null) {
            return;
        }
        Map<Long, FormGroupPathSnapshot> groupSnapshotMap = getGroupSnapshotMapIfNeeded(List.of(rule));
        applyRuleSnapshot(existing, rule, groupSnapshotMap);
        recordMapper.updateById(existing);
        // 删除原有的明细数据
        detailMapper.delete(Wrappers.<FormAppraisalRecordDetail>lambdaQuery().eq(FormAppraisalRecordDetail::getRecordId, recordId));
        // 重新构建明细数据(也会更新主记录数据)
        buildRecordDetails(existing, rule, Period.of(existing.getAssessmentBeginTime(), existing.getAssessmentEndTime()));
    }

    /**
     * 为单个规则生成考核记录
     * 如果记录已存在且不是生成状态则跳过
     *
     * @param rule             考核规则
     * @param assessmentYear   考核年度
     * @param assessmentPeriod 考核期数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSingleRuleRecord(FormAppraisalRule rule, Integer assessmentYear, Integer assessmentPeriod) {
        Map<Long, FormGroupPathSnapshot> groupSnapshotMap = getGroupSnapshotMapIfNeeded(List.of(rule));
        generateSingleRuleRecord(rule, assessmentYear, assessmentPeriod, groupSnapshotMap);
    }

    private void generateSingleRuleRecord(FormAppraisalRule rule, Integer assessmentYear, Integer assessmentPeriod,
                                          Map<Long, FormGroupPathSnapshot> groupSnapshotMap
                                         ) {
        // 查询是否已存在该规则的考核记录
        FormAppraisalRecord existing = recordMapper.selectOne(Wrappers.<FormAppraisalRecord>lambdaQuery()
                                                                      .eq(FormAppraisalRecord::getRuleId, rule.getId())
                                                                      .eq(FormAppraisalRecord::getPeriodType, rule.getPeriodType())
                                                                      .eq(FormAppraisalRecord::getAssessmentYear, assessmentYear)
                                                                      .eq(FormAppraisalRecord::getAssessmentPeriod, assessmentPeriod)
                                                                      .last("limit 1"));
        // 如果记录已存在且不是生成状态，说明已经被处理过，跳过
        if (existing != null && existing.getStatus() != AppraisalRecordStatusEnum.GENERATED) {
            return;
        }
        // 计算目标周期的时间范围
        AppraisalPeriodUtils.TargetPeriod targetPeriod = AppraisalPeriodUtils.targetPeriod(rule.getPeriodType(), assessmentYear, assessmentPeriod);
        // 创建新记录或使用现有记录
        FormAppraisalRecord record = existing == null ? new FormAppraisalRecord() : existing;
        // 如果是新记录，生成唯一 ID
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        // 设置考核周期信息
        record.setAssessmentYear(assessmentYear);
        record.setAssessmentPeriod(assessmentPeriod);
        record.setAssessmentBeginTime(targetPeriod.getBeginTime());
        record.setAssessmentEndTime(targetPeriod.getEndTime());
        // 设置规则基本信息
        applyRuleSnapshot(record, rule, groupSnapshotMap);
        // 设置原始分数（来自规则定义）
        record.setOriginSpotCheckScore(rule.getSpotCheckScore());
        record.setOriginInspectionScore(rule.getInspectionScore());
        record.setOriginTotalScore(rule.getTotalScore());
        // 初始化最终分数为原始分数（后续会根据采集数据重新计算）
        record.setFinalSpotCheckScore(rule.getSpotCheckScore());
        record.setFinalInspectionScore(rule.getInspectionScore());
        record.setFinalTotalScore(rule.getTotalScore());
        // 设置记录状态为生成状态
        record.setStatus(AppraisalRecordStatusEnum.GENERATED);
        record.setGeneratedAt(LocalDateTime.now());
        record.setCreatorType(ActorTypeEnum.SYSTEM);
        record.setUpdaterType(ActorTypeEnum.SYSTEM);
        // 保存或更新记录
        if (existing == null) {
            // 新记录，插入数据库
            recordMapper.insert(record);
        } else {
            // 已存在，先删除原有明细数据，再更新主记录
            detailMapper.delete(Wrappers.<FormAppraisalRecordDetail>lambdaQuery()
                                        .eq(FormAppraisalRecordDetail::getRecordId, record.getId()));
            recordMapper.updateById(record);
        }
        // 构建明细数据（采集数据、匹配扣分、计算分数、回写主数据）
        buildRecordDetails(record, rule, Period.of(targetPeriod.getBeginTime(), targetPeriod.getEndTime()));
    }

    /**
     * 构建记录明细
     * 采集数据、匹配扣分区间、计算最终得分
     *
     * @param record 考核记录实体
     * @param rule   考核规则实体
     * @param period 考核周期
     */
    private void buildRecordDetails(FormAppraisalRecord record, FormAppraisalRule rule, Period period) {
        var tenantId = SessionContextHolder.getRequester().getTenant();
        SessionContextHolder.clearSessionContext();
        AppraisalRecordCalculateResult calculateResult = calculateService.calculate(rule, period);
        SessionContextHolder.requestTenant(tenantId);
        for (AppraisalRecordDetailCalculateResult detailResult : calculateResult.getDetails()) {
            FormAppraisalRecordDetail detail = FormAppraisalRecordDetail.builder()
                    .id(IdWorker.getId())
                    .recordId(record.getId())
                    .ruleContentId(detailResult.getRuleContentId())
                    .ruleItemId(detailResult.getRuleItemId())
                    .contentType(detailResult.getContentType())
                    .sourceType(detailResult.getSourceType())
                    .sourceRefId(detailResult.getSourceRefId())
                    .sourceRefName(detailResult.getSourceRefName())
                    .contentDesc(detailResult.getContentDesc())
                    .standardDesc(detailResult.getStandardDesc())
                    .itemName(detailResult.getItemName())
                    .fieldKey(detailResult.getFieldKey())
                    .fieldName(detailResult.getFieldName())
                    .calcType(detailResult.getCalcType())
                    .rawValue(detailResult.getRawValue())
                    .matchedRangeText(detailResult.getMatchedRangeText())
                    .deductionReasonJson(detailResult.getDeductionReasonJson())
                    .originDeductScore(detailResult.getOriginDeductScore())
                    .manualAdjustDeductScore(java.math.BigDecimal.ZERO)
                    .finalDeductScore(detailResult.getFinalDeductScore())
                    .sort(detailResult.getSort())
                    .build();
            detailMapper.insert(detail);
        }
        record.setFinalSpotCheckScore(calculateResult.getFinalSpotCheckScore());
        record.setFinalInspectionScore(calculateResult.getFinalInspectionScore());
        record.setFinalTotalScore(calculateResult.getFinalTotalScore());
        // 计算后重置覆盖分
        record.setOverrideFinalTotalScore(null);
        record.setOverrideFinalSpotCheckScore(null);
        record.setOverrideFinalInspectionScore(null);
        record.setStatus(AppraisalRecordStatusEnum.CONFIRMED);
        // 更新记录（回写计算后的分数）
        recordMapper.updateById(record);
    }

    private void applyRuleSnapshot(FormAppraisalRecord record, FormAppraisalRule rule, Map<Long, FormGroupPathSnapshot> groupSnapshotMap) {
        record.setRuleId(rule.getId());
        record.setRuleVersion(rule.getRuleVersion());
        record.setName(rule.getName());
        record.setInspectionItem(rule.getInspectionItem());
        record.setPeriodType(rule.getPeriodType());
        List<Long> departmentIds = AppraisalDepartmentUtils.parseDepartmentIds(rule.getResponsibleDepartmentIds());
        record.setResponsibleDepartmentIds(AppraisalDepartmentUtils.joinDepartmentIds(departmentIds));
        record.setResponsibleDepartmentNames(AppraisalDepartmentUtils.joinDepartmentNames(
                organizationIntegrationService.getDepartmentNames(departmentIds)
                                                                                         ));
        applyGroupSnapshot(record, rule, groupSnapshotMap);
    }

    private Map<Long, FormGroupPathSnapshot> getGroupSnapshotMapIfNeeded(List<FormAppraisalRule> rules) {
        if (rules == null || rules.stream().noneMatch(rule -> rule != null && Objects.nonNull(rule.getFormGroupId()))) {
            return Collections.emptyMap();
        }
        return formFlowGroupIntegrationService.getGroupPathSnapshotMap();
    }

    private void applyGroupSnapshot(FormAppraisalRecord record, FormAppraisalRule rule, Map<Long, FormGroupPathSnapshot> groupSnapshotMap) {
        record.setFirstLevelFormGroupId(null);
        record.setFirstLevelFormGroupName(null);
        record.setSecondLevelFormGroupId(null);
        record.setSecondLevelFormGroupName(null);
        Long formGroupId = rule.getFormGroupId();
        if (Objects.isNull(formGroupId)) {
            return;
        }
        FormGroupPathSnapshot snapshot = groupSnapshotMap.get(formGroupId);
        if (Objects.isNull(snapshot)) {
            log.warn("生成表单考核记录时未解析到所属分组, ruleId={}, formGroupId={}, periodType={}, assessmentYear={}, assessmentPeriod={}",
                     rule.getId(),
                     formGroupId,
                     rule.getPeriodType(),
                     record.getAssessmentYear(),
                     record.getAssessmentPeriod());
            return;
        }
        record.setFirstLevelFormGroupId(snapshot.getFirstLevelFormGroupId());
        record.setFirstLevelFormGroupName(snapshot.getFirstLevelFormGroupName());
        record.setSecondLevelFormGroupId(snapshot.getSecondLevelFormGroupId());
        record.setSecondLevelFormGroupName(snapshot.getSecondLevelFormGroupName());
    }
}
