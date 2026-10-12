package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class InstanceListByGenreDTO {
    private String id;
    private String genreName;
    private Integer count;

    public InstanceListByGenreDTO(String id, String genreName, Integer count) {
        this.genreName = genreName;
        this.count = count;
        this.id = id;
    }

    private List<Instance> instanceList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Instance {
        private String code;
        private String deviceGenreName;
        private String parentId;
        private String deviceId;
        private String deviceInstanceName;
        private String areaId;
        private String areaName;
        private LinkedList<String> areaIdList;
    }
}
