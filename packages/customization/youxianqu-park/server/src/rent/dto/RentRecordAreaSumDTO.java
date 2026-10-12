package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.dto;

import lombok.Data;

/**
 * 租控记录面积统计
 */
@Data
public class RentRecordAreaSumDTO {
    /**
     * 出售面积
     */
    private String roomAreaCalculate;
    /**
     * 房间状态（RentRoomStatusEnum）
     * 0闲置、1已租、2预定、16已售
     */
    private String status;
}
