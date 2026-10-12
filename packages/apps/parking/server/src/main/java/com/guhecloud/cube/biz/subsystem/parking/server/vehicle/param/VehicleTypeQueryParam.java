package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 16:42
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleTypeQueryParam extends Paging {

    private String keyword;
}
