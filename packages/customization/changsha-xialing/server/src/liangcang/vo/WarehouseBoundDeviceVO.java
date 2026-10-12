package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 仓库已绑定业务设备。 */
@Data
public class WarehouseBoundDeviceVO {
    /** 业务设备 id。 */
    private Long deviceId;
    /** 设备类型。 */
    private String deviceType;
    /** 产品名称。 */
    private String productName;
    /** 设备名称。 */
    private String deviceName;
    /** 区域 id 路径。 */
    private String areaId;
    /** 区域名称路径。 */
    private String areaName;
}
