package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;

import java.util.LinkedList;

@Data
public class DeviceGenreDTO {
    private Long deviceId;
    private String deviceInstanceName;
    private Long genreId;
    private String genreName;
    private String areaId;
    private String areaName;
    private String code;
    private LinkedList<Long> areaIdList;
}