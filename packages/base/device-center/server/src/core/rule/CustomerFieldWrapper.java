package com.guhecloud.cube.base.device.center.core.rule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;

public interface CustomerFieldWrapper<T> {

    QueryWrapper<T> buildCustomerFieldWrapper(String tableAlias, RuleGroup ruleGroup);
}
