package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.message;

import lombok.Data;

@Data
public class SecurityDeviceDeleteMessage {
    public final static String TOPIC = "SECURITY_DELETE_DEVICE";

    //模块
    private String applicationCode;

    //设备id
    private Long deviceId;
}
