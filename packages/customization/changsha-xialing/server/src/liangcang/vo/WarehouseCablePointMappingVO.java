package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 电缆点位映射详情。 */
@Data
public class WarehouseCablePointMappingVO {
    private Long pointId;
    private Long cableId;
    private Long warehouseId;
    private Integer pointNo;
    private Long deviceId;
    /** 设备中心设备编码，用作页面物联设备编号展示。 */
    private String deviceCode;
    private String deviceName;
    /** mapped / unmapped。 */
    private String status;
}
