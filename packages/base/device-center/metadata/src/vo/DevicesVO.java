package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

@Data
public class DevicesVO {
    //设备id
    private Long deviceId;
    //编码
    private String deviceCode;
    //设备名称
    private String deviceName;
    private Long genreId;
    /**
     * 设备类型
     */
    private String genreName;
    //区域
    private String areaName;
    //区域id
    private String areaId;

    //状态，中文
    private String deviceStatus;

    //状态，枚举值
    private Integer deviceStatusCode;

    /**
     * 经纬度
     */
    private String lntAndLat;
    /**
     * 第三方设备标识
     */
    private String thirdPartyId;
    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;
}