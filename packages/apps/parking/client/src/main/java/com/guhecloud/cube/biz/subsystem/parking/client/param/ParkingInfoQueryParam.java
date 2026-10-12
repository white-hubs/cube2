package com.guhecloud.cube.biz.subsystem.parking.client.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 17:01
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInfoQueryParam {
    /**
     * 车场编号
     */
    protected String parkingInfoCode;

    /**
     * 区域主键
     */
    protected String areaId;

    /**
     * 车场编号详情
     */
    protected LinkedHashSet<String> parkingInfoCodes;

    private String keyword;
}
