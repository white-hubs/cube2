package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/7/6 17:48
 * @description
 */
@Data
public class SnapshotDTO {
    private String deviceId;
    private String propCode;
    private String propValue;
    private String propValueTs;
    private String condition;
}
