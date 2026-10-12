package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ElectricityConnectionParam {
    private Long id;
    private String lineName;
    /**
     * 开始节点id
     */
    private Long fromNodeId;
    /**
     * 结束节点id
     */
    private Long toNodeId;
    /**
     * 网页样式(前端定位用)
     */
    private String webStyle;
    private String createBy;
    private LocalDateTime createTime;
}
