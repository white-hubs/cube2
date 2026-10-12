package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto;

import lombok.Data;

@Data
public class EventDeviceDetailDTO {

    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 类型id
     */
    private Long genreId;

    /**
     * 类型名称
     */
    private String genreName;

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 经纬度
     */
    private String lntAndLat;
}
