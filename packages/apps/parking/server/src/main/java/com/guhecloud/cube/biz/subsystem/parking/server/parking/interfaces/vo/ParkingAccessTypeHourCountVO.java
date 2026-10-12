package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import lombok.Data;

/**
 * 通行趋势按进出类型 + 小时聚合行。
 */
@Data
public class ParkingAccessTypeHourCountVO {

    private ParkingAccessTypeEnum accessType;

    private Integer hourOfDay;

    private Integer count;
}
