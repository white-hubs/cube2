package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitWorkOvertimeRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;

/**
 * @author bq
 * @date 2022/11/11
 * @description
 */
public interface WorkOvertimeRuleCommandService extends IService<WorkOvertimeRulePO> {

    Long saveOrUpdate(SubmitWorkOvertimeRuleCommand command);

    void delete(Long id);

}
