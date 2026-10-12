package com.guhecloud.subsystem.requester.sdk.vo.patrol;

import lombok.Data;

@Data
public class PatrolRouteVO {
    private String id;
    //线路ID
    private String lineId;
    private String lineName;
    //巡检点ID
    private String pointId;
    private String pointName;
    //顺序号
    private int order;
}
