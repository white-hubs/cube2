package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LineLossConnectionParam {
    private Long id;
    private String lineName;
    /**
     * 开始节点 id
     */
    private Long fromNodeId;
    /**
     * 结束节点 id
     */
    private Long toNodeId;
    private Long fromDeviceId;
    private Long toDeviceId;
    /**
     * 网页样式 (前端定位用)
     */
    private String webStyle;
    private String createBy;
    private LocalDateTime createTime;
}
