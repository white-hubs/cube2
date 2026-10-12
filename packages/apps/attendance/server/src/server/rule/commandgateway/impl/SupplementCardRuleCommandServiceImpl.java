package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.SupplementCardRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitSupplementCardRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.SupplementCardRuleOperationService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.SupplementCardRuleValidator;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bq
 * @date 2022/11/10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplementCardRuleCommandServiceImpl extends ServiceImpl<SupplementCardRuleMapper, SupplementCardRulePO> implements SupplementCardRuleCommandService {

    private final SupplementCardRuleValidator validator;
    private final SupplementCardRuleOperationService supplementCardRuleOperationService;

    @Override
    public Long saveOrUpdate(SubmitSupplementCardRuleCommand command) {
        Long id = command.getId();
        validator.verifyIsExistByName(id, command.getName());
        if (null == id) {
            SupplementCardRulePO rulePO = ObjectUtils.copying(command, SupplementCardRulePO.class);
            rulePO.setType(String.join(",", command.getTypes()));
            supplementCardRuleOperationService.insertOperation(rulePO);
            this.save(rulePO);
            id = rulePO.getId();
        } else {
            SupplementCardRulePO saved = super.getById(id);
            SupplementCardRulePO oldPO = new SupplementCardRulePO();
            ObjectUtils.copying(saved, oldPO);
            if (saved != null) {
                ObjectUtils.copying(command, saved);
                saved.setType(String.join(",", command.getTypes()));
                supplementCardRuleOperationService.updateOperation(oldPO, saved);
                this.updateById(saved);
            }
        }
        return id;
    }

    @Override
    public void delete(Long id) {
        supplementCardRuleOperationService.deleteOperation(this.getById(id));
        DatabaseContextHolder.softDelete(this, id);
    }

}
