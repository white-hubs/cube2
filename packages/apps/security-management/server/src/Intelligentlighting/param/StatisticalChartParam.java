package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param;

import lombok.Data;

import java.util.List;

@Data
public class StatisticalChartParam {
    private Long sceneType;
    private Long areaId;
    private List<String> businessIds;

}
