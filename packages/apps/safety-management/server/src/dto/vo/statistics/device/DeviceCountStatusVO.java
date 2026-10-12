package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device;

import lombok.Data;

/**
 * 设备状态计数
 */
@Data
public class DeviceCountStatusVO {
    private String status;

    private Integer count;
}
