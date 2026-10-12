package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:38
 * @Description:
 */

@Data
public class VisualizationBaseQueryParam {
    private Long projectId;
    private String keyword;
    private Long areaId;
}
