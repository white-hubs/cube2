package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import lombok.Data;

/**
 * 通行趋势按进出类型和时间桶聚合行。
 */
@Data
public class ParkingAccessTypeBucketCountVO {

    private ParkingAccessTypeEnum accessType;

    private String bucket;

    private Long count;
}
