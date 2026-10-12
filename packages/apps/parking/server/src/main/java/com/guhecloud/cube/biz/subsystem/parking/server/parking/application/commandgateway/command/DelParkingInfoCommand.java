package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class DelParkingInfoCommand {

    private Set<Long> ids;
}
