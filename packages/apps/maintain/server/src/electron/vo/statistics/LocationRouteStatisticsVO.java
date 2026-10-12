package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 巡更路线统计
 * @date 2024/1/24 20:17
 */
@Data
public class LocationRouteStatisticsVO {
    /**
     * 巡更路线id
     */
    private Long routeId;
    /**
     * 巡更路线名称
     */
    private String routeName;
    /**
     * 巡更次数
     */
    private Integer keepNum;
}
