package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

@Data
public class InstanceManageParam {
    /**
     * id
     */
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 实例名称
     */
    private String instanceName;
    /**
     * 状态
     */
    private String status;
}
