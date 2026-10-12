package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.CompareOpEnum;
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
@TableName("device_notify_rule_condition_exp")
public class DeviceNotifyRuleConditionExpPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 条件主键
     */
    private Long conditionId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性单位
     */
    private String propUnit;

    /**
     * 比较符（= >=）
     */
    private CompareOpEnum op;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 延时时间
     */
    private Integer delayTime;

    /**
     * 延迟开始时间
     */
    private Integer startDelayTime;

    /**
     * 延迟结束时间
     */
    private Integer endDelayTime;
}
