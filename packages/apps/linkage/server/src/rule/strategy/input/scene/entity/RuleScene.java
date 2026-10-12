package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 场景规则
 *
 * @author lanchun
 * @date 2022/6/30 9:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RuleScene extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 密码
     */
    private String password;

}
