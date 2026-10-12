package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

/**
 * 设备数据共享配置-新增参数
 */
@Data
public class DevDataShareCreateParam {
    /**
     * 源设备ID
     */
    private Long sourceDeviceId;

    /**
     * 数据类型：1-属性 2-事件
     */
    private Integer sourceDataType;

    /**
     * 源数据ID（属性ID或事件ID）
     */
    private Long sourceDataId;

    /**
     * 目标设备ID
     */
    private Long targetDeviceId;

    /**
     * 目标数据ID（属性ID或事件ID）
     */
    private Long targetDataId;

    /**
     * 共享模式：1-共享 2-转换
     */
    private Integer shareMode = 1;

    /**
     * 备注
     */
    private String remark;
}

