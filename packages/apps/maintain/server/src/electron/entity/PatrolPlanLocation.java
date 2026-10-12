package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_plan_location")
public class PatrolPlanLocation extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 执行顺序
     */
    private Integer orderNum;
}
