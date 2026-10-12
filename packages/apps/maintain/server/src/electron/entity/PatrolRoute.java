package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_route")
public class PatrolRoute extends BasicEnhancedTenantEntity<Long> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 巡更路线名称
     */
    private String patrolRouteName;
    /**
     * 巡更点id
     */
    private Long patrolLocationId;
    /**
     * 顺序
     */
    private Integer ruleOrder;
    private String status;
    private Long uuid;
    private Integer number;

}
