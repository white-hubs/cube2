package com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplementCardRuleValidator {

    private final SupplementCardRuleMapper supplementCardRuleMapper;

    /**
     * 验证补卡规则名称是否相同
     *
     * @param id   规则ID
     * @param name 名称
     */
    public void verifyIsExistByName(Long id, String name) {
        SupplementCardRulePO po = supplementCardRuleMapper.selectOne(Wrappers.<SupplementCardRulePO>lambdaQuery()
                                                                             .eq(SupplementCardRulePO::getName, name)
                                                                             .ne(null != id, SupplementCardRulePO::getId, id)
                                                                    );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

}
