package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.LogicalOpEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("device_notify_rule_condition")
public class DeviceNotifyRuleConditionPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 条件类型(事件或者自定条件)
     */
    private DeviceRuleConditionTypeEnum conditionType;

    /**
     * 设备事件主键
     */
    private String deviceEventId;

    /**
     * 设备事件编号
     */
    private String deviceEventCode;

    /**
     * 设备事件名称
     */
    private String deviceEventName;

    /**
     * 设备事件类型
     */
    private String deviceEventType;

    /**
     * 逻辑运算符（or and）
     */
    private LogicalOpEnum logicalOp;

    /**
     * 设备通知规则主键
     */
    private Long ruleId;
}
