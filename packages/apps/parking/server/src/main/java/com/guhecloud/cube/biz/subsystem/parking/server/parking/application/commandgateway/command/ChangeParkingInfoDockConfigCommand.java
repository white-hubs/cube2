package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command;

import lombok.Data;

@Data
public class ChangeParkingInfoDockConfigCommand {

    /**
     * 车场id
     */
    private Long id;

    /**
     * 车场对接配置
     */
    private String config;
}
