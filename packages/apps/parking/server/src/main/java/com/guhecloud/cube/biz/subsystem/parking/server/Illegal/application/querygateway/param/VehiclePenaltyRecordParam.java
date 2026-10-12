package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 15:34
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VehiclePenaltyRecordParam extends Paging {

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 违停时间
     */
    private Period violationTime;
}
