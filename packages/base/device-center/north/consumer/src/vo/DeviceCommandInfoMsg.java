package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

/**
 * 命令执行信息
 *
 * @author Fl
 * @date 2022-05-06 16:44
 **/
@Data
public class DeviceCommandInfoMsg {
    private String msgId;
    private String instanceId;
    private String deviceId;
    private String propCode;
    private String deviceName;
    private CommandResponse commandResponse;
}
