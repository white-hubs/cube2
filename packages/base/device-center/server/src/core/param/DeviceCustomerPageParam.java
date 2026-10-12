package com.guhecloud.cube.base.device.center.core.param;

import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceCustomerPageParam extends Paging {

    /**
     * 自定义条件
     */
    private RuleGroup ruleGroup;
}
