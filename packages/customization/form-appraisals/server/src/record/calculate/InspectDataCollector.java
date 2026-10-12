package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormAppraisalInspectIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.cube.srv.gen.maintain.client.InspectPlanClient;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 巡检数据采集器
 * 从巡检系统中采集任务相关数据
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectDataCollector implements AppraisalDataCollector {
    private final FormAppraisalInspectIntegrationService inspectIntegrationService;
    private final InspectPlanClient inspectPlanClient;

    /**
     * 判断是否支持巡检数据源
     *
     * @param sourceType 数据来源类型
     *
     * @return true-支持
     */
    @Override
    public boolean supports(AppraisalSourceTypeEnum sourceType) {
        // 仅当来源类型为 INSPECT 时支持
        return sourceType == AppraisalSourceTypeEnum.INSPECT;
    }

    /**
     * 采集巡检数据
     * 支持任务总数、超时任务数、异常项数等指标
     *
     * @param content 规则内容
     * @param item    规则项
     * @param period  考核周期
     *
     * @return 采集结果
     */
    @Override
    public AppraisalCollectResult collect(FormAppraisalRuleContent content, FormAppraisalRuleItem item, Period period) {
        var sourceRefName = inspectPlanClient.getPlanName(Long.valueOf(content.getSourceRefId()));
        content.setSourceRefName(sourceRefName);
        // 根据计算类型从巡检集成服务获取对应的统计值
        BigDecimal value = switch (item.getCalcType()) {
            // 任务总数：获取考核周期内的所有任务数量
            case TASK_COUNT -> inspectIntegrationService.getTaskCount(period, content.getSourceRefId());
            // 超时任务数：获取考核周期内超时的任务数量
            case TIMEOUT_TASK_COUNT -> inspectIntegrationService.getTimeoutTaskCount(period, content.getSourceRefId());
            // 异常项数：获取考核周期内的异常项目数量
            case ABNORMAL_ITEM_COUNT -> inspectIntegrationService.getAbnormalItemCount(period, content.getSourceRefId());
            // 默认情况返回 0
            default -> BigDecimal.ZERO;
        };
        // 返回采集结果，包含数值化结果和字符串形式的原始值（去除末尾零，不使用科学计数法）
        return new AppraisalCollectResult(value, value.stripTrailingZeros().toPlainString());
    }
}
