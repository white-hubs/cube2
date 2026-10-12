package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租控记录查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RentRecordQueryParam extends Paging {
    /**
     * 房间状态(RentRoomStatusEnum)
     */
    private String status;
}
