package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 应急设备统计
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyDeviceStatVO {
    private int total;
    private int usedValue;
    private int notUsedValue;

    public int getNotUsedValue() {
        int temp = total - usedValue;
        return temp > 0 ? temp : 0;
    }
}