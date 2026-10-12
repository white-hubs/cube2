package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 应急设施统计
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyFacilityStatVO {
    private int total;
    private int usedValue;
    private int notUsedValue;
}