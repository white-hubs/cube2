package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param;

import com.guhecloud.cube.biz.subsystem.parking.server.common.query.StatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleBottomMonitorRecordStatisticsQuery extends StatisticsQuery {

    /**
     * 进or出
     */
    private ParkingAccessTypeEnum accessType;
}
