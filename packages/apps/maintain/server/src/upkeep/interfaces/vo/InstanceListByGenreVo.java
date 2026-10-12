package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class InstanceListByGenreVo {
    private Long id;
    private String genreName;
    private Integer count;
    private List<Instance> instanceList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Instance {
        private String code;
        private String deviceGenreName;
        private Long parentId;
        private Long deviceId;
        private String deviceInstanceName;
        private String areaId;
        private String areaName;
        private LinkedList<Long> areaIdList;
        private List<MaintenanceProjectVo> projectVos = new ArrayList<>();
    }
}
