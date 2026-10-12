package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.rocketmq.message;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/11 15:49
 * @Description:
 */

@Data
public class ProjectScheduleMessage {
    /**
     * 工程进度完成消息通知
     */
    public static final String TOPIC = "project_schedule_complete";
    private Integer tenant;
    private Long projectId;
    private String projectName;
}
