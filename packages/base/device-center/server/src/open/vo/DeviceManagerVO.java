package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 设备管理信息
 * @date 2023/12/4 16:08
 */
@Data
public class DeviceManagerVO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 管理人员ids
     */
    private String managerIds;
}
