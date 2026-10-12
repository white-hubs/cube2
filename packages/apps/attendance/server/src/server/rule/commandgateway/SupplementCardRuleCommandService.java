package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitSupplementCardRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;

/**
 * @author bq
 * @date 2022/11/10
 * @description
 */
public interface SupplementCardRuleCommandService extends IService<SupplementCardRulePO> {

    Long saveOrUpdate(SubmitSupplementCardRuleCommand command);

    void delete(Long id);

}
