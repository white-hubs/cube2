package com.guhecloud.cube.biz.ouaa.server.behavior.entity;

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
public class UserBehavior extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 用户行为配置
     */
    private String behaviorConfig;
}
