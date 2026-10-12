package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 电缆点位可选设备。 */
@Data
public class WarehouseCableAvailableDeviceVO {
    private Long deviceId;
    /** 设备中心设备编码，用作页面物联设备编号展示。 */
    private String deviceCode;
    private String deviceName;
    private String deviceType;
    /** 是否已被电缆点位映射。 */
    private Boolean mapped;
    private Long mappedCableId;
    private String mappedCableNo;
    private Integer mappedPointNo;
    /** mapped / unmapped。 */
    private String status;
}
