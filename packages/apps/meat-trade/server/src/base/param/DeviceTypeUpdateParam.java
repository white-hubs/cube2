package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

/**
 * 设备类型编辑
 *
 * @author lzy
 * @date 2021/9/13
 */
@Data
public class DeviceTypeUpdateParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备类型名称
     */
    private String name;

    /**
     * 类别
     */
    private String type;
}
