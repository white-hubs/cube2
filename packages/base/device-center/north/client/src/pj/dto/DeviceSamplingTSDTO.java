package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.Map;

/**
 * 设备采样数据
 */
@Data
public class DeviceSamplingTSDTO {
    private Map<String, KeyValueWithTsDTO> propValues;
    /**
     * 时间戳
     */
    private long ts;
}
