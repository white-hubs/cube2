package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/29 11:17
 * @Description: 区域下设备数量
 */

@Data
public class AreaDeviceNumVO {
    /**
     * 区域Id
     */
    private String areaId;

    /**
     * 设备数量
     */
    private Integer deviceNum;
}
