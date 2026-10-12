package com.guhecloud.cube.biz.linkage.server.record.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventDevice {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备区域
     */
    private String areaName;

    /**
     * 设备经纬度
     */
    private String lntAndLat;

    /**
     * 设备位置信息
     */
    private String address;
}
