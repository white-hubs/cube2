package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** warehousePage deviceList 单项。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDeviceInfoVO {
    private Integer id;
    private Integer warehouseId;
    private String name;
    private String deviceType;
    private String state;
    private String workState;
    private String deviceNum;
    private Long scanTime;
    private String rotateSpeed;
}
