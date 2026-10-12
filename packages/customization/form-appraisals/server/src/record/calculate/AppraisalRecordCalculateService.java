package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.platform.base.model.Period;

/**
 * 表单考核记录纯计算服务。
 */
public interface AppraisalRecordCalculateService {
    /**
     * 按规则和统计周期计算考核结果，不执行落库。
     *
     * @param rule   考核规则
     * @param period 统计周期
     * @return 计算结果
     */
    AppraisalRecordCalculateResult calculate(FormAppraisalRule rule, Period period);
}
