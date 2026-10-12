package com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeRuleValidator {

    private final WorkOvertimeRuleMapper workOvertimeRuleMapper;

    /**
     * 验证规则名称是否相同
     *
     * @param id   规则ID
     * @param name 名称
     */
    public void verifyIsExistByName(Long id, String name) {
        WorkOvertimeRulePO po = workOvertimeRuleMapper.selectOne(Wrappers.<WorkOvertimeRulePO>lambdaQuery()
                                                                         .eq(WorkOvertimeRulePO::getName, name)
                                                                         .ne(null != id, WorkOvertimeRulePO::getId, id)
                                                                );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

}
