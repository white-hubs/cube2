package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("broadcast_play_rule_template")
public class BroadcastPlayRuleTemplatePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 模板主键
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;
}
