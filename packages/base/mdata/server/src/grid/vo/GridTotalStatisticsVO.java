package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.Data;

/**
 * 网格总统计 VO
 */
@Data
public class GridTotalStatisticsVO {

    /**
     * 网格数
     */
    private Long gridCount = 0L;

    /**
     * 网格长数
     */
    private Long gridLeaderCount = 0L;

    /**
     * 网格员数
     */
    private Long gridMemberCount = 0L;

    /**
     * 设备数
     */
    private Long deviceCount = 0L;

    /**
     * 资产数
     */
    private Long assetCount = 0L;
}
