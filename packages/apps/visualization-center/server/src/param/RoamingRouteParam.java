package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:46
 * @Description:
 */
@Data
public class RoamingRouteParam {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 漫游路线名称
     */
    private String name;
}
