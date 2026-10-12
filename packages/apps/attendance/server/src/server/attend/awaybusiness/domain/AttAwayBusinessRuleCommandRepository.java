package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.domain;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  09:17
 * @Description:
 * @Version: 1.0
 */
public interface AttAwayBusinessRuleCommandRepository {
    void awayBusinessRuleAdd(AttAwayBusinessRulePO newRulePo);

    void awayBusinessRuleEdit(AttAwayBusinessRulePO oldRulePo, AttAwayBusinessRulePO newRulePo);

    void awayBusinessRuleRemove(AttAwayBusinessRulePO awayBusinessRulePO);
}
