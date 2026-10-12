package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration;

import lombok.Getter;

/**
 * 设备控制状态枚举
 *
 * @author lanchun
 * @date 2022/6/25 15:06
 */
@Getter
public enum DeviceCommandStatusEnum {
    INIT("初始状态，尚未进行操作"),
    PROCESSING("在进行本地处理"),
    ACCEPT("命令已被接收（远程处理是异步的情况下，远端通知命令已被接收"),
    SUCCESS("命令已处理成功"),
    FAIL("命令已处理失败"),
    EXPIRED("在进行本地处理状态下未及时收到远端确认，或等待远程确认命令执行结果超时");

    /**
     * 描述信息
     */
    private final String description;

    DeviceCommandStatusEnum(String description) {
        this.description = description;
    }
}
