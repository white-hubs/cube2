package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result;

import lombok.Data;

/**
 * 租控记录返回对象
 */
@Data
public class RentRecordResult {
    private Long id;
    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 出售面积
     */
    private String roomAreaCalculate;

    /**
     * 房间单位名
     */
    private String roomUnitName;

    /**
     * 房间状态（RentRoomStatusEnum）
     * 0闲置、1已租、2预定、16已售
     */
    private String status;

    /**
     * 预定状态
     * 0、未预定 1、已预定
     */
    private String reserveStatus;

    /**
     * 锁定状态
     * 0、未锁定 1、已锁定
     */
    private String lockedStatus;

    /**
     * 房间缴费状态
     * 0正常缴费、1欠费
     */
    private String payStatus;

    /**
     * 合同类型
     */
    private String contractType;
}
