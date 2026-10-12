package com.guhecloud.cube.biz.subsystem.parking.client.vo;

import lombok.Data;

import java.util.List;

/**
 * 车辆批量入参
 */
@Data
public class VehicleInfoBatchParam {
    private List<VehicleInfoVO> vehicleInfoList;
}
