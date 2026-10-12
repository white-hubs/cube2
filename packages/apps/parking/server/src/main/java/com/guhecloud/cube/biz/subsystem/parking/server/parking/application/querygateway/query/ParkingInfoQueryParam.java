package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ParkingInfoQueryParam extends ParkingBaseQueryParam {
    private String keyword;

    /**
     * 来源
     */
    private ParkingSourceEnum source;
}
