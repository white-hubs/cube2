package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.SubmitWorkOvertimeRuleOperationVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitWorkOvertimeRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 加班规则操作
 * @date 2023/6/13 17:14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOverTimeRuleOperationService extends ServiceImpl<WorkOvertimeRuleMapper, WorkOvertimeRulePO> {

    /**
     * @Author wql
     * @Description 新增加班规则
     * @Date 17:23 2023/6/13
     **/
    @LogRecord(success = "新增:{{#command.name}}", subType = "OVERTIME_RULE", subTypeName = "加班规则", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#command.id}}"
    )
    public void insertOperation(SubmitWorkOvertimeRuleCommand command) {

    }

    /**
     * @Author wql
     * @Description 更新加班规则
     * @Date 17:50 2023/6/13
     **/
    @LogRecord(success = "更新:{{#newVO.name}},{_DIFF{#oldVO,#newVO}}", subType = "OVERTIME_RULE", subTypeName = "加班规则",
               operationType = "UPDATE", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newVO.id}}"
    )
    public void updateOvertimeRule(SubmitWorkOvertimeRuleOperationVO oldVO, SubmitWorkOvertimeRuleOperationVO newVO) {

    }

    /**
     * @Author wql
     * @Description 删除加班规则
     * @Date 17:50 2023/6/13
     **/
    @LogRecord(success = "删除:{{#po.name}}", subType = "OVERTIME_RULE", subTypeName = "加班规则", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#po.id}}"
    )
    public void deleteOperation(WorkOvertimeRulePO po) {

    }
}
