package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

@Data
public class MaintenanceUpdatePlanCommand {
    private Long id;
    private String status;
}
