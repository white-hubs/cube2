package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.request;

import lombok.Data;

import java.util.Set;

/**
 * 获取设备属性值参数
 *
 * @author lanchun
 * @date 2023/11/7 15:35
 */
@Data
public class GetDevicePropRequest {

    /**
     * 设备主键集合
     */
    private Set<Long> deviceIds;

    /**
     * 属性编号
     */
    private String propCode;
}
