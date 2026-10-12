package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeviceInfo {

    /**
     * 设备主键
     */
    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 第三方设备标识
     */
    private String thirdPartyCode;

    public DeviceInfo() {
    }
}
