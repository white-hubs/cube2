package com.guhecloud.cube.biz.linkage.server.rule.base.entity;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.DateUnitEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.RuleTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 规则数据模型
 *
 * @author lanchun
 * @date 2022/6/21 10:18
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Rule extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则编号
     */
    private String code;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 规则类型
     */
    private RuleTypeEnum type;

    /**
     * 业务类型
     */
    private BusinessTypeEnum businessType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否去重
     */
    private Boolean reiterated;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private DateUnitEnum repeatTimeUnit;

    /**
     * 触发级别主键
     */
    private Long triggerLevelId;

    /**
     * 触发级别名称
     */
    private String triggerLevelName;

    /**
     * 触发类型主键
     */
    private Long triggerTypeId;

    /**
     * 触发类型名称
     */
    private String triggerTypeName;

    /**
     * 触发次数
     */
    private Integer triggerNum = 0;
}
