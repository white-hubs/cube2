package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceSchemaTipsModelVo {
    private String tenantId;
    /**
     * 设备编码
     */
    private String devNum;
    /**
     * 设备名称
     */
    private String devName;
    /**
     * 是否是顶级设备
     */
    private boolean isRoot;

    /**
     * 一些描述<br/>
     * 例如: 设备拥有哪些属性、事件、方法等
     */
    private String schema;

    private String parentDevNum;

    private String parentDevName;

    private Date insertTime;
}
