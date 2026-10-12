package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleBottomMonitorRecordQueryParam extends Paging {

    /**
     * 关键字查询(支持车牌号、来访事由)
     */
    private String keyword;

    /**
     * 事由编号
     */
    private String reasonType;

    /**
     * 时间
     */
    private Period period;

    /**
     * 进or出
     */
    private ParkingAccessTypeEnum accessType;
}
