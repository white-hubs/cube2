package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomParam {

    /**
     * 名称
     */
    private String name;

    /**
     * 房间类型ID
     */
    private Long roomTypeId;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 状态
     */
    private RoomStatusEnum status;

    /**
     * 建筑面积
     */
    private double floorage;

    /**
     * 计费面积
     */
    private double floorageBilling;

    /**
     * 文件IDs
     */
    private List<Long> fileIds;
}
