package com.guhecloud.cube.srv.gen.appraisals.server.record.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalDepartmentUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalPeriodUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormFlowGroupIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.OrganizationIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.dto.FormGroupPathSnapshot;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordCalculateResult;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordCalculateService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.calculate.AppraisalRecordDetailCalculateResult;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalPreviewRecordDetailMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalPreviewRecordMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalPreviewRecord;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalPreviewRecordDetail;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalPreviewRecordGenerateService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 表单考核预览记录生成保障服务实现。
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalPreviewRecordGenerateServiceImpl implements FormAppraisalPreviewRecordGenerateService {
    private final FormAppraisalRuleMapper ruleMapper;
    private final FormAppraisalPreviewRecordMapper previewRecordMapper;
    private final FormAppraisalPreviewRecordDetailMapper previewDetailMapper;
    private final FormFlowGroupIntegrationService formFlowGroupIntegrationService;
    private final OrganizationIntegrationService organizationIntegrationService;
    private final AppraisalRecordCalculateService calculateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCurrentPreviews() {
        LocalDateTime nowTime = LocalDateTime.now();
        ensurePreview(AppraisalPeriodTypeEnum.MONTHLY, nowTime);
        ensurePreview(AppraisalPeriodTypeEnum.QUARTERLY, nowTime);
        ensurePreview(AppraisalPeriodTypeEnum.HALF_YEAR, nowTime);
    }

    @Transactional(rollbackFor = Exception.class)
    protected AppraisalPeriodUtils.TargetPeriod ensurePreview(AppraisalPeriodTypeEnum periodType, LocalDateTime previewAsOfTime) {
        AppraisalPeriodUtils.TargetPeriod targetPeriod = AppraisalPeriodUtils.currentPeriodUntil(periodType, previewAsOfTime);
        List<FormAppraisalRule> rules = ruleMapper.selectList(Wrappers.<FormAppraisalRule>lambdaQuery()
                                                                      .eq(FormAppraisalRule::getPeriodType, periodType)
                                                                      .eq(FormAppraisalRule::getEnabled, true));
        Map<Long, FormGroupPathSnapshot> groupSnapshotMap = getGroupSnapshotMapIfNeeded(rules);
        for (FormAppraisalRule rule : rules) {
            generateSingleRulePreview(rule, targetPeriod, groupSnapshotMap);
        }
        return targetPeriod;
    }

    private FormAppraisalPreviewRecord generateSingleRulePreview(FormAppraisalRule rule, AppraisalPeriodUtils.TargetPeriod targetPeriod,
                                                                 Map<Long, FormGroupPathSnapshot> groupSnapshotMap
                                                                ) {
        FormAppraisalPreviewRecord existing = selectExisting(rule, targetPeriod);
        if (existing != null && Objects.equals(existing.getPreviewAsOfTime(), targetPeriod.getEndTime()) &&
            Objects.equals(existing.getRuleVersion(), rule.getRuleVersion())) {
            return existing;
        }

        FormAppraisalPreviewRecord record = existing == null ? new FormAppraisalPreviewRecord() : existing;
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        record.setAssessmentYear(targetPeriod.getAssessmentYear());
        record.setAssessmentPeriod(targetPeriod.getAssessmentPeriod());
        record.setAssessmentBeginTime(targetPeriod.getBeginTime());
        record.setAssessmentEndTime(targetPeriod.getEndTime());
        record.setPreviewAsOfTime(targetPeriod.getEndTime());
        record.setGeneratedAt(LocalDateTime.now());
        record.setCreatorType(ActorTypeEnum.SYSTEM);
        record.setUpdaterType(ActorTypeEnum.SYSTEM);
        applyRuleSnapshot(record, rule, groupSnapshotMap);

        AppraisalRecordCalculateResult calculateResult = calculateService.calculate(
                rule,
                Period.of(targetPeriod.getBeginTime(), targetPeriod.getEndTime())
                                                                                   );
        record.setOriginSpotCheckScore(calculateResult.getOriginSpotCheckScore());
        record.setOriginInspectionScore(calculateResult.getOriginInspectionScore());
        record.setOriginTotalScore(calculateResult.getOriginTotalScore());
        record.setFinalSpotCheckScore(calculateResult.getFinalSpotCheckScore());
        record.setFinalInspectionScore(calculateResult.getFinalInspectionScore());
        record.setFinalTotalScore(calculateResult.getFinalTotalScore());

        if (existing == null) {
            try {
                previewRecordMapper.insert(record);
            } catch (DuplicateKeyException e) {
                FormAppraisalPreviewRecord duplicate = selectExisting(rule, targetPeriod);
                if (duplicate == null) {
                    throw e;
                }
                record.setId(duplicate.getId());
                previewRecordMapper.updateById(record);
                previewDetailMapper.delete(Wrappers.<FormAppraisalPreviewRecordDetail>lambdaQuery()
                                                   .eq(FormAppraisalPreviewRecordDetail::getRecordId, record.getId()));
            }
        } else {
            previewRecordMapper.updateById(record);
            previewDetailMapper.delete(Wrappers.<FormAppraisalPreviewRecordDetail>lambdaQuery()
                                               .eq(FormAppraisalPreviewRecordDetail::getRecordId, record.getId()));
        }
        insertDetails(record.getId(), calculateResult.getDetails());
        return record;
    }

    private FormAppraisalPreviewRecord selectExisting(FormAppraisalRule rule, AppraisalPeriodUtils.TargetPeriod targetPeriod) {
        return previewRecordMapper.selectOne(Wrappers.<FormAppraisalPreviewRecord>lambdaQuery()
                                                     .eq(FormAppraisalPreviewRecord::getRuleId, rule.getId())
                                                     .eq(FormAppraisalPreviewRecord::getPeriodType, rule.getPeriodType())
                                                     .eq(FormAppraisalPreviewRecord::getAssessmentYear, targetPeriod.getAssessmentYear())
                                                     .eq(FormAppraisalPreviewRecord::getAssessmentPeriod, targetPeriod.getAssessmentPeriod())
                                                     .last("limit 1"));
    }

    private void insertDetails(Long recordId, List<AppraisalRecordDetailCalculateResult> detailResults) {
        for (AppraisalRecordDetailCalculateResult detailResult : detailResults) {
            FormAppraisalPreviewRecordDetail detail = FormAppraisalPreviewRecordDetail.builder()
                    .id(IdWorker.getId())
                    .recordId(recordId)
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
                    .manualAdjustDeductScore(BigDecimal.ZERO)
                    .finalDeductScore(detailResult.getFinalDeductScore())
                    .sort(detailResult.getSort())
                    .build();
            previewDetailMapper.insert(detail);
        }
    }

    private void applyRuleSnapshot(FormAppraisalPreviewRecord record, FormAppraisalRule rule,
                                   Map<Long, FormGroupPathSnapshot> groupSnapshotMap
                                  ) {
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

    private void applyGroupSnapshot(FormAppraisalPreviewRecord record, FormAppraisalRule rule,
                                    Map<Long, FormGroupPathSnapshot> groupSnapshotMap
                                   ) {
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
            log.warn("生成表单考核预览记录时未解析到所属分组, ruleId={}, formGroupId={}, periodType={}, assessmentYear={}, assessmentPeriod={}",
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
