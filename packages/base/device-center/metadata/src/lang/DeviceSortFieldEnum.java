package com.guhecloud.cube.base.device.center.metadata.lang;

import lombok.Getter;

@Getter
public enum DeviceSortFieldEnum {

    /**
     * 设备状态排序
     */
    ONLINE_STATUS("device_state");

    private final String tableField;

    DeviceSortFieldEnum(String tableField) {this.tableField = tableField;}
}
