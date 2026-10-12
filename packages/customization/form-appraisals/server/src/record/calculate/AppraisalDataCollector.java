package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.platform.base.model.Period;

/**
 * 考核数据采集器接口
 * 定义不同数据来源的采集器规范
 */
public interface AppraisalDataCollector {
    /**
     * 判断是否支持指定的来源类型
     *
     * @param sourceType 数据来源类型
     * @return true-支持 false-不支持
     */
    boolean supports(AppraisalSourceTypeEnum sourceType);

    /**
     * 采集考核数据
     *
     * @param content 规则内容
     * @param item    规则项
     * @param period  考核周期
     * @return 采集结果
     */
    AppraisalCollectResult collect(FormAppraisalRuleContent content, FormAppraisalRuleItem item, Period period);
}
