package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-26 15:39
 */
@Data
public class DeviceStatusUpdateMsg {

    private String deviceId;

    private List<KeyValueWithTs> updates;
}
