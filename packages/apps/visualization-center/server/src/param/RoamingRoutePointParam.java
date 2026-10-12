package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 13:44
 * @Description:
 */
@Data
public class RoamingRoutePointParam {

    private Long id;

    /**
     * 漫游路线id
     */
    private Long routeId;

    /**
     * 漫游点位名称
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
