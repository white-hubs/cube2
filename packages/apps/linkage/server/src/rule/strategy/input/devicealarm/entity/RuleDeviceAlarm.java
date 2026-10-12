package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity;

import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.enumeration.RuleConfigTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 设备报警规则条件
 *
 * @author lanchun
 * @date 2022/6/23 9:55s
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RuleDeviceAlarm extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     *
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 规则配置类型
     */
    private RuleConfigTypeEnum type;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 操作符
     */
    private String op;

    /**
     * 状态持续时延
     */
    private Integer delayTime;
}
