package com.guhecloud.cube.base.visualization.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:15
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoamingRouteVO {

    /**
     * 漫游路线id
     */
    private Long id;

    /**
     * 漫游路线名称
     */
    private String name;

    /**
     * 漫游路线点列表
     */
    private List<RoamingRoutePointVO> roamingRoutePointList;
}
