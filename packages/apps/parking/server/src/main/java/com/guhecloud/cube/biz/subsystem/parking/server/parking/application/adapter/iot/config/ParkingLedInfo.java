package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.config;

import lombok.Data;

/**
 * 车场信息
 */
@Data
public class ParkingLedInfo {
    /**
     * 车场名称
     */
    private String name;

    /**
     * 车场ID
     */
    private String parkingId;

    /**
     * LED区域ID
     */
    private String ledAreaId;

    /**
     * 内容模板，支持占位符：
     * ${{parkingSpaceNum}} - 剩余车位数
     * ${{parkingTotalNum}} - 车位总数
     */
    private String contentTemplate;
}
