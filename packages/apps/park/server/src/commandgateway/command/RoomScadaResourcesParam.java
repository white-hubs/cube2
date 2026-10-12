package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Getter;

@Getter
public class RoomScadaResourcesParam {

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 二维组态地址
     */
    private String url;
}
