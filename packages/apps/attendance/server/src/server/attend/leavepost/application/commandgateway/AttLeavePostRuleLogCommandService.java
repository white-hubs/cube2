package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostRuleLogCommandService {

    @LogRecord(success = "新增:{{#rulePO.name}}", subType = "POST_RULE", subTypeName = "离岗规则", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#rulePO.id}}"
    )
    public void addLog(AttLeavePostRulePO rulePO) {
        log.info("添加离岗规则操作日志：{}", rulePO);
    }

    @LogRecord(success = "更新:{_DIFF{#oldPO,#newPO}}", subType = "POST_RULE", operationType = "UPDATE",
               subTypeName = "离岗规则", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public void updateLog(AttLeavePostRulePO newPO, AttLeavePostRulePO oldPO) {
        log.info("修改离岗规则操作日志：{}{}", newPO, oldPO);
    }

    @LogRecord(success = "删除:{{#rulePO.name}}", subType = "POST_RULE", subTypeName = "离岗规则", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#rulePO.id}}"
    )
    public void deleteLog(AttLeavePostRulePO rulePO) {
        log.info("删除离岗规则操作日志：{}", rulePO);
    }
}
