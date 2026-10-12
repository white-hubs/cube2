package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.domain.AttAwayBusinessRuleCommandRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  09:18
 * @Description:
 * @Version: 1.0
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessRuleCommandRepositoryImpl implements AttAwayBusinessRuleCommandRepository {
    @Override
    @LogRecord(success = "新增:{{#newRulePo.name}}", subType = "AWAY_RULE", subTypeName = "出差规则", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newRulePo.id}}"
    )
    public void awayBusinessRuleAdd(AttAwayBusinessRulePO newRulePo) {
    }

    @Override
    @LogRecord(success = "更新:{_DIFF{#oldRulePo,#newRulePo}}", subType = "AWAY_RULE", subTypeName = "出差规则", operationType = "UPDATE",
               typeName = "考勤系统", type = "ATTENDANCE", bizNo = "{{#newRulePo.id}}"
    )
    public void awayBusinessRuleEdit(AttAwayBusinessRulePO oldRulePo, AttAwayBusinessRulePO newRulePo) {
    }

    @Override
    @LogRecord(success = "删除:{{#awayBusinessRulePO.name}}", subType = "AWAY_RULE", subTypeName = "出差规则", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#awayBusinessRulePO.id}}"
    )
    public void awayBusinessRuleRemove(AttAwayBusinessRulePO awayBusinessRulePO) {

    }
}
