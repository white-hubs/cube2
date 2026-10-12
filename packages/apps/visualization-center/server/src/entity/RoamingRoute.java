package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 漫游路线
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:00
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("roaming_route")
public class RoamingRoute extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 路线名称
     */
    private String name;

}
