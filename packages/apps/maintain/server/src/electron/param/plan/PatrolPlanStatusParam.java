package com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan;

import lombok.Data;

@Data
public class PatrolPlanStatusParam {
    private Long id;
    /**
     * 状态:ENABLE("启用"),
     * STOP("停用")
     */
    private String status;
}
