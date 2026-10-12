package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class MaintenanceProgetRecordCommand {

    private Long id;

    private Integer tenant;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 维保项
     */
    private String maintrenaceProject;
    /**
     * 维保记录
     */
    @Size(max = 200, message = "维保结果不能超过200")
    private String record;
    /**
     * 维保描述
     */
    @Size(max = 1000, message = "维保描述不能超过1000")
    private String maintenanceDescribe;

    /**
     * 维保人
     */
    private String maintenancePerson;

    /**
     * 维保人id
     */
    private Long maintenancePersonId;

}
