package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dto.FormAppraisalDeductionReasonDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleContentMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleItemMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.RangeConfigDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 表单考核记录纯计算服务实现。
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppraisalRecordCalculateServiceImpl implements AppraisalRecordCalculateService {
    private final FormAppraisalRuleContentMapper contentMapper;
    private final FormAppraisalRuleItemMapper itemMapper;
    private final List<AppraisalDataCollector> dataCollectors;
    private final DeductionMatcher deductionMatcher;

    @Override
    public AppraisalRecordCalculateResult calculate(FormAppraisalRule rule, Period period) {
        List<FormAppraisalRuleContent> contents = contentMapper.selectList(Wrappers.<FormAppraisalRuleContent>lambdaQuery()
                                                                                   .eq(FormAppraisalRuleContent::getRuleId, rule.getId())
                                                                                   .orderByAsc(FormAppraisalRuleContent::getSort));
        List<FormAppraisalRuleItem> items = itemMapper.selectList(Wrappers.<FormAppraisalRuleItem>lambdaQuery()
                                                                          .eq(FormAppraisalRuleItem::getRuleId, rule.getId())
                                                                          .orderByAsc(FormAppraisalRuleItem::getSort));
        BigDecimal totalDeduct = BigDecimal.ZERO;
        BigDecimal spotDeduct = BigDecimal.ZERO;
        BigDecimal inspectionDeduct = BigDecimal.ZERO;

        AppraisalRecordCalculateResult result = new AppraisalRecordCalculateResult();
        result.setOriginTotalScore(rule.getTotalScore());
        result.setOriginSpotCheckScore(rule.getSpotCheckScore());
        result.setOriginInspectionScore(rule.getInspectionScore());

        for (FormAppraisalRuleContent content : contents) {
            for (FormAppraisalRuleItem item : items.stream().filter(i -> content.getId().equals(i.getContentId())).toList()) {
                AppraisalCollectResult collectResult = collect(content, item, period);
                DeductionMatchResult matchResult = deductionMatcher.match(item.getRangeConfigJson(), collectResult.getNumericValue());
                BigDecimal deductScore = matchResult.getDeductScore();
                totalDeduct = totalDeduct.add(deductScore);
                if (content.getContentType() == AppraisalContentTypeEnum.SPOT_CHECK) {
                    spotDeduct = spotDeduct.add(deductScore);
                } else {
                    inspectionDeduct = inspectionDeduct.add(deductScore);
                }

                AppraisalRecordDetailCalculateResult detail = new AppraisalRecordDetailCalculateResult();
                detail.setRuleContentId(content.getId());
                detail.setRuleItemId(item.getId());
                detail.setContentType(content.getContentType());
                detail.setSourceType(content.getSourceType());
                detail.setSourceRefId(content.getSourceRefId());
                detail.setSourceRefName(content.getSourceRefName());
                detail.setContentDesc(content.getContentDesc());
                detail.setStandardDesc(content.getStandardDesc());
                detail.setItemName(item.getItemName());
                detail.setFieldKey(item.getFieldKey());
                detail.setFieldName(item.getFieldName());
                detail.setCalcType(item.getCalcType());
                detail.setRawValue(collectResult.getRawValue());
                detail.setMatchedRangeText(matchResult.getMatchedRangeText());
                detail.setDeductionReasonJson(buildDeductionReasonJson(content, item, collectResult, matchResult));
                detail.setOriginDeductScore(deductScore);
                detail.setFinalDeductScore(deductScore);
                detail.setSort(item.getSort());
                result.getDetails().add(detail);
            }
        }

        result.setFinalSpotCheckScore(rule.getSpotCheckScore().subtract(spotDeduct).max(BigDecimal.ZERO));
        result.setFinalInspectionScore(rule.getInspectionScore().subtract(inspectionDeduct).max(BigDecimal.ZERO));
        result.setFinalTotalScore(rule.getTotalScore().subtract(totalDeduct).max(BigDecimal.ZERO));
        return result;
    }

    private AppraisalCollectResult collect(FormAppraisalRuleContent content, FormAppraisalRuleItem item, Period period) {
        var collector = dataCollectors.stream()
                .filter(c -> c.supports(content.getSourceType()))
                .findFirst()
                .orElse(null);
        Assert.isParameterError(collector == null, "不支持的数据来源：" + content.getSourceType());
        return collector.collect(content, item, period);
    }

    private String buildDeductionReasonJson(FormAppraisalRuleContent content, FormAppraisalRuleItem item,
                                            AppraisalCollectResult collectResult,
                                            DeductionMatchResult matchResult
                                           ) {
        if (matchResult == null || matchResult.getDeductScore() == null ||
            matchResult.getDeductScore().compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        try {
            RangeConfigDTO matchedRange = findMatchedRange(item.getRangeConfigJson(),
                                                           collectResult == null ? null : collectResult.getNumericValue());
            if (matchedRange == null) {
                return null;
            }
            FormAppraisalDeductionReasonDTO reason = new FormAppraisalDeductionReasonDTO();
            reason.setSourceRefName(content.getSourceRefName());
            reason.setFieldKey(item.getFieldKey());
            reason.setFieldName(item.getItemName());
            reason.setFieldValue(collectResult == null ? null : collectResult.getRawValue());
            reason.setMatchedRangeStartValue(matchedRange.getStartValue());
            reason.setMatchedRangeEndValue(matchedRange.getEndValue());
            reason.setDeductScore(matchResult.getDeductScore());
            return JSON.toJSONString(reason);
        } catch (Exception e) {
            log.warn("生成表单考核记录扣分原因失败, ruleContentId={}, ruleItemId={}",
                     content == null ? null : content.getId(),
                     item == null ? null : item.getId(),
                     e);
            return null;
        }
    }

    private RangeConfigDTO findMatchedRange(String rangeConfigJson, BigDecimal numericValue) {
        if (rangeConfigJson == null || rangeConfigJson.isBlank()) {
            return null;
        }
        BigDecimal actualValue = numericValue == null ? BigDecimal.ZERO : numericValue;
        List<RangeConfigDTO> ranges = JSON.parseArray(rangeConfigJson, RangeConfigDTO.class);
        if (ranges == null || ranges.isEmpty()) {
            return null;
        }
        for (RangeConfigDTO range : ranges) {
            if (range.getStartValue() == null || range.getEndValue() == null || range.getDeductScore() == null) {
                continue;
            }
            if (actualValue.compareTo(range.getStartValue()) >= 0 && actualValue.compareTo(range.getEndValue()) < 0) {
                return range;
            }
        }
        return null;
    }
}
