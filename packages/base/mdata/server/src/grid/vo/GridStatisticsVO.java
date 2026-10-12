package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.Data;

/**
 * 网格统计信息 VO
 */
@Data
public class GridStatisticsVO {

    /**
     * 网格数
     */
    private Long gridCount;

    /**
     * 当班网格长数量
     */
    private Long onDutyGridLeaderCount;
}

