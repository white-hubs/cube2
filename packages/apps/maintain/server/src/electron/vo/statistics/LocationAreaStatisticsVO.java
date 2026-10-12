package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 巡更点区域统计
 * @date 2024/1/24 19:46
 */
@Data
public class LocationAreaStatisticsVO {
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 巡更点数量
     */
    private Integer num;
    /**
     * 占比
     */
    private Float proportion;
}
