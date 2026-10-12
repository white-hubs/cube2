package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DevDataShareVo {
    private Long id;

    /**
     * 源设备信息
     */
    private Long sourceDeviceId;
    private String sourceDeviceName;
    private Long sourceProductId;
    private String sourceProductName;
    private Integer sourceDataType;
    private Long sourceDataId;
    private String sourceDataCode;

    /**
     * 目标设备信息
     */
    private Long targetDeviceId;
    private String targetDeviceName;
    private Long targetProductId;
    private String targetProductName;
    private Long targetDataId;
    private String targetDataCode;

    /**
     * 共享配置
     */
    private Integer shareMode;
    private String remark;
    private LocalDateTime createTime;
}

