package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.aggregate.AttSchedulingRulesAggregate;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.SchedulingRuleOperationVO;

public interface AttSchedulingRuleRepository {

    /**
     * 新增排班规则
     *
     * @param aggregate 规则聚合
     *
     * @return id
     */
    Long insertAttSchedulingRule(AttSchedulingRulesAggregate aggregate);

    void addPOLog(Long id, String name);

    void addUpdateRulePOLog(SchedulingRuleOperationVO newPo, SchedulingRuleOperationVO oldPo);

    void addDeleteRulePOLog(AttSchedulingRulePO rulePO);

    /**
     * 编辑排班规则
     *
     * @param aggregate 规则聚合
     *
     * @return id
     */
    Long updateAttSchedulingRule(AttSchedulingRulesAggregate aggregate);

    /**
     * 删除排班规则
     *
     * @param id 规则主键
     *
     * @return id
     */
    Long deleteAttSchedulingRule(Long id);
}
