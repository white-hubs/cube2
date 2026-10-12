package com.guhecloud.cube.srv.gen.loose.works.message;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 10:55
 * @Description: 工程进度小消息
 */
@Data
@Builder
public class ProjectScheduleMessage {
    /**
     * 工程进度完成消息通知
     */
    public static final String TOPIC = "project_schedule_complete";

    /**
     * 工程id
     */
    private Long projectId;

    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 租户
     */
    private Integer tenant;
}
