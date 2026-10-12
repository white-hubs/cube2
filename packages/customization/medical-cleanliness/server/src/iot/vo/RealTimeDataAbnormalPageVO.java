package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-23  16:59
 * @Description:
 * @Version: 1.0
 */
@Data
public class RealTimeDataAbnormalPageVO {
    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 异常指标数
     */
    private Integer value;
}
