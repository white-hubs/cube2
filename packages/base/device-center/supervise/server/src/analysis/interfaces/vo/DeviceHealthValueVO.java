package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/10 16:21
 * @description
 */
@Data
public class DeviceHealthValueVO {
    private Long deviceId;
    private Integer healthValue;
}
