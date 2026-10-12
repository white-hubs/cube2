package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.Data;

/**
 * 网格区域统计 VO
 */
@Data
public class GridAreaStatisticsVO {

    /**
     * 网格区域id
     */
    private Long gridAreaId;

    /**
     * 网格名称
     */
    private String gridAreaName;

    /**
     * 网格长名称，多个用、拼接
     */
    private String gridLeaderName = "";

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

    /**
     * 进行中任务数
     */
    private Long processingTaskCount = 0L;

    /**
     * 完成任务数
     */
    private Long finishedTaskCount = 0L;

    /**
     * 异常任务数
     */
    private Long abnormalTaskCount = 0L;

    /**
     * 未完成工单数
     */
    private Long unfinishedOrderCount = 0L;

    /**
     * 报警数
     */
    private Long alarmCount = 0L;
}
