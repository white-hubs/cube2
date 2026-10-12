package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.config;

import lombok.Data;
import java.util.List;

/**
 * 引导屏车场配置
 */
@Data
public class PlazaConfig {
    /**
     * URL地址
     */
    private String url;

    /**
     * 车场信息列表
     */
    private List<ParkingLedInfo> parkingInfo;

    /**
     * 展示模式
     */
    private String displayMode;

    /**
     * 是否清屏幕
     */
    private Boolean clearScreen;
}
