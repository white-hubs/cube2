package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备规则条件分页查询参数，语义对齐 {@code DeviceCustomerPageParam} 的 rule 条件；
 * {@code ruleGroup} 为 {@code RuleGroup} 的 JSON 结构，通常来自 {@code GET /tag/rule/{code}} 返回的规则组。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceRuleConditionPageParam extends Paging {

    /**
     * 规则组 JSON，反序列化为 RuleGroup 后参与设备筛选
     */
    private Object ruleGroup;
}
