package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import lombok.Data;

@Data
public class ModifyParkingInfoDockConfigCommand {

    /**
     * 车场id
     */
    private Long id;

    /**
     * 厂家编码
     */
    private ParkingProviderEnum providerCode;

    /**
     * 车场对接配置
     */
    private String config;
}
