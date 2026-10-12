package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class DelTrafficDetectionAreaCommand {

    private Set<Long> ids;
}
