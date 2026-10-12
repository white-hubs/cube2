package com.guhecloud.cube.base.device.center.open.interfaces.web.vo;

import lombok.Data;

/**
 * 设备分组返回参数
 *
 * @author lanchun
 * @date 2025/4/14 15:33
 */
@Data
public class DeviceGroupPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分组名
     */
    private String name;

    /**
     * 描叙
     */
    private String remark;

}
