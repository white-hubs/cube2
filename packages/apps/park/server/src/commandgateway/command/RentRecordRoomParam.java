package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Data;

@Data
public class RentRecordRoomParam {

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 房间名称
     */
    private String roomName;

}
