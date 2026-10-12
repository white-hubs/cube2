package com.guhecloud.cube.base.device.center.core.rule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.rule.entity.Rule;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleField;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DeviceCustomerFieldWrapper implements CustomerFieldWrapper<BizDevice> {

    @Override
    public QueryWrapper<BizDevice> buildCustomerFieldWrapper(String tableAlias, RuleGroup ruleGroup) {
        QueryWrapper<BizDevice> deviceQueryWrapper = new QueryWrapper<>();
        if (Objects.isNull(ruleGroup) || CollectionUtils.isEmpty(ruleGroup.getRules())) {
            return deviceQueryWrapper;
        }
        for (Rule rule : ruleGroup.getRules()) {
            // 如果不是第一个条件，则使用 or() 来拼接
            if (!deviceQueryWrapper.isEmptyOfWhere()) {
                deviceQueryWrapper.or();
            }
            deviceQueryWrapper.nested(q -> parseConditionWrapper(tableAlias, q, rule.getRuleFields()));
        }
        return deviceQueryWrapper;
    }

    public static void parseConditionWrapper(String tableAlias,
                                             QueryWrapper<BizDevice> queryWrapper,
                                             List<RuleField> ruleFields
                                            ) {
        for (RuleField field : ruleFields) {
            String fileId = field.getField().getTableColumn();
            if (StringUtils.isNotBlank(tableAlias)) {
                fileId = tableAlias + "." + fileId;
            }
            // 根据条件类型应用不同的条件
            switch (field.getOperator()) {
                case IN:
                    queryWrapper.in(fileId, (Collection<?>) field.getValue());
                    break;
                case NOT_IN:
                    queryWrapper.notIn(fileId, (Collection<?>) field.getValue());
                    break;
                case LIKE:
                    queryWrapper.like(fileId, field.getValue());
                    break;
                case EQ:
                    queryWrapper.eq(fileId, field.getValue());
                    break;
                case GREATER_THAN:
                    queryWrapper.gt(fileId, field.getValue());
                    break;
                case GREATER_THAN_OR_EQUAL:
                    queryWrapper.ge(fileId, field.getValue());
                    break;
                case LESS_THAN:
                    queryWrapper.lt(fileId, field.getValue());
                    break;
                case LESS_THAN_OR_EQUAL:
                    queryWrapper.le(fileId, field.getValue());
                    break;
                default:
                    queryWrapper.eq(fileId, field.getValue());
                    break;
            }
        }
    }
}
