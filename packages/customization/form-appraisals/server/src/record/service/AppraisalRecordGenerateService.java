package com.guhecloud.cube.srv.gen.appraisals.server.record.service;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;

import java.time.LocalDateTime;

/**
 * 考核记录生成服务接口
 * 负责定时生成考核记录
 */
public interface AppraisalRecordGenerateService {
    /**
     * 根据周期类型自动生成考核记录
     * 基于当前时间判断是否应该生成上一周期的记录
     *
     * @param periodType   周期类型
     * @param finalNowTime
     */
    void generateByPeriod(AppraisalPeriodTypeEnum periodType, LocalDateTime finalNowTime);

    /**
     * 根据指定的年度和期数生成考核记录
     *
     * @param periodType       周期类型
     * @param assessmentYear   考核年度
     * @param assessmentPeriod 考核期数
     */
    void generateByTargetPeriod(AppraisalPeriodTypeEnum periodType, Integer assessmentYear, Integer assessmentPeriod);

    /**
     * 在允许的条件下重新生成记录
     * 仅当记录处于生成状态时才允许重新生成
     *
     * @param recordId 记录 ID
     */
    void regenerateIfAllowed(Long recordId);

    /**
     * 为单个规则生成考核记录
     *
     * @param rule             考核规则
     * @param assessmentYear   考核年度
     * @param assessmentPeriod 考核期数
     */
    void generateSingleRuleRecord(FormAppraisalRule rule, Integer assessmentYear, Integer assessmentPeriod);
}
