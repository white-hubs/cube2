package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 联动预案动作数据模型
 *
 * @author lanchun
 * @date 2024/6/15 18:41
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LinkageApplication extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则事件编号
     */
    private String ruleEventCode;

    /**
     * 规则事件分类编号
     */
    private String ruleEventTypeCode;

    /**
     * 规则对象区域
     */
    private String ruleTargetArea;

    /**
     * 规则主键
     */
    private String ruleId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用规则触发主题
     */
    private String applicationRuleTriggerTopic;

    /**
     * 应用规则主键
     */
    private Long applicationRuleId;

}
