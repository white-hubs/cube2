package com.guhecloud.cube.base.visualization.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:27
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoamingRoutePointVO {

    private Long id;

    /**
     * 线路id
     */
    private Long routeId;

    /**
     * 线路名称
     */
    private String routeName;

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
