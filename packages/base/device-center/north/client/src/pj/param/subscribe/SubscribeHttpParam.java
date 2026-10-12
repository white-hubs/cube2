package com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe;

import lombok.Data;

import java.util.List;

@Data
public class SubscribeHttpParam {
    private List<DeviceCommandCondition> deviceCommandConditions;
    private List<DeviceEventCondition> deviceEventConditions;
    private List<DevicePropCondition> devicePropConditions;
    private List<DeviceStatusCondition> deviceStatusConditions;

    private String callbackUrl;// 回调地址
    private long ttsWithSecond;// 订阅有效期，单位秒
}
