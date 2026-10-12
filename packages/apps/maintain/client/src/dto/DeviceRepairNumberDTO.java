package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2024/1/23 9:20
 * @description 设备维修次数
 */
@Data
public class DeviceRepairNumberDTO {
    private Long deviceId;

    private Integer number;
}
