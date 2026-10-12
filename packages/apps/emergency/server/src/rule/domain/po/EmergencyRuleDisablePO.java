package com.guhecloud.cube.biz.emergency.server.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 规则禁用配置
 *
 * @author lanchun
 * @date 2024/11/11 15:04
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_rule_disable")
public class EmergencyRuleDisablePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 禁用时长(小时)
     */
    private Integer duration;

    /**
     * 打开时间
     */
    private LocalDateTime openTime;
}
