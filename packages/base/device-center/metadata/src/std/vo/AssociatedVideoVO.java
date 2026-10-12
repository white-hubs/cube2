package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

@Data
public class AssociatedVideoVO {
    /**
     * 监控token(第三方设备id)
     */
    private String thirdPartyId;
    /**
     * 监控名称
     */
    private String name;
    /**
     * 设备ID
     */
    private Long id;
    /**
     * 设备孪生编码
     */
    private String deviceCode;
}
