package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoomRentRecordParam {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 签订时间
     */
    private LocalDateTime signTime;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 房间
     */
    private List<RentRecordRoomParam> rentRooms;

    /**
     * 文件ID
     */
    private List<Long> fileIds;

}
