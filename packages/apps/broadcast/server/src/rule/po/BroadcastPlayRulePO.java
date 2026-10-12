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
@TableName("broadcast_play_rule")
public class BroadcastPlayRulePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 规则描述
     */
    private String remark;
}
