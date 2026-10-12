package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.Data;

/**
 * 区域对象数统计 VO
 */
@Data
public class GridAreaObjectCountVO {

    /**
     * 区域id
     */
    private Long gridAreaId;

    /**
     * 设备数
     */
    private Long deviceCount;

    /**
     * 档位数
     */
    private Long stallCount;

    /**
     * 资产数
     */
    private Long propertyCount;

    /**
     * 现场点数
     */
    private Long scenePointCount;

    /**
     * 巡更点数
     */
    private Long patrolLocationCount;
}

