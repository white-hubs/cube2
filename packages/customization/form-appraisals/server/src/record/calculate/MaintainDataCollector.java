package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormAppraisalMaintainIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.cube.srv.gen.maintain.client.MaintainPlanClient;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 运维数据采集器
 * 从运维系统中采集超时任务数据
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintainDataCollector implements AppraisalDataCollector {
    private final FormAppraisalMaintainIntegrationService maintainIntegrationService;
    private final MaintainPlanClient maintainPlanClient;

    /**
     * 判断是否支持运维数据源
     *
     * @param sourceType 数据来源类型
     *
     * @return true-支持
     */
    @Override
    public boolean supports(AppraisalSourceTypeEnum sourceType) {
        // 仅当来源类型为 MAINTAIN 时支持
        return sourceType == AppraisalSourceTypeEnum.MAINTAIN;
    }

    /**
     * 采集运维数据
     * 获取超时任务数量
     *
     * @param content 规则内容
     * @param item    规则项
     * @param period  考核周期
     *
     * @return 采集结果
     */
    @Override
    public AppraisalCollectResult collect(FormAppraisalRuleContent content, FormAppraisalRuleItem item, Period period) {
        var sourceRefName = maintainPlanClient.getPlanName(Long.valueOf(content.getSourceRefId()));
        content.setSourceRefName(sourceRefName);
        // 从运维集成服务获取考核周期内的超时任务数量
        BigDecimal value = maintainIntegrationService.getTimeoutTaskCount(period, content.getSourceRefId());
        // 返回采集结果，包含数值化结果和字符串形式的原始值（去除末尾零，不使用科学计数法）
        return new AppraisalCollectResult(value, value.stripTrailingZeros().toPlainString());
    }
}
