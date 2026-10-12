package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

@Data
public class WaxPoint {
    private Long id;//巡检点ID
    private String name;//巡检点名称
    private String card;//巡检点卡号
    private Long areaId;//区域ID
    private String areaName;//区域名称
}
