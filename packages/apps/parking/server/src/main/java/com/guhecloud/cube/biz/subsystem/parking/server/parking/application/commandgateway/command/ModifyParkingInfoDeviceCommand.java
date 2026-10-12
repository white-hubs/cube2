package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class ModifyParkingInfoDeviceCommand {

    /**
     * 设备id集合
     */
    private Set<Long> deviceIds;
}
