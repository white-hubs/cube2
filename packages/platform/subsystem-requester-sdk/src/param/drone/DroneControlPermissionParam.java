package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneControlPermissionParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 授权开关：true 获取飞机控制权限，false 释放飞机控制权限。
     */
    private Boolean authorized;
}
