package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaxLine {
    private Long id;//线路ID
    private String name;//线路名称
    private String areaId;//区域ID
    private String areaName;//区域名称
    private List<Device> devices;//巡检器列表
    private List<String> checkpointIds;//巡更点IDS

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Device {
        private Long id;
        private String name;
        private String code;
        private String areaId;
        private String areaName;
    }
}
