package com.guhecloud.cube.srv.gen.device.supervise.client.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/10 16:21
 * @description
 */
@Data
public class DeviceHealthValueDTO {
    private Long deviceId;
    private Integer healthValue;
}
