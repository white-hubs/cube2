package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import lombok.Data;

import java.util.List;

@Data
public class PatrolRouteParam {
    /**
     * 巡更路线id
     */
    private Long id;
    /**
     * 巡更路线名称
     */
    private String patrolRouteName;
    /**
     * 巡更点id集合
     */
    private List<PatrolRouteParam> locationIds;
}
