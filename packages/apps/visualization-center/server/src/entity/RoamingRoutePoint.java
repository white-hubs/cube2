package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:09
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("roaming_route_point")
public class RoamingRoutePoint extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 线路id
     */
    private Long routeId;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 视角参数
     */
    private String perspective;

    /**
     * 排序
     */
    private Integer sort;

    private BigDecimal speed;
}
