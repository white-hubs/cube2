package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.Data;

@Data
public class MaintenanceProgetRecordVo {

    private Long id;
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
    private String record;
    /**
     * 维保描述
     */
    private String maintenanceDescribe;
}
