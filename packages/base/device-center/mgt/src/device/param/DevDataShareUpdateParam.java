package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备数据共享配置-修改参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DevDataShareUpdateParam extends DevDataShareCreateParam {
    /**
     * 主键ID
     */
    private Long id;
}

