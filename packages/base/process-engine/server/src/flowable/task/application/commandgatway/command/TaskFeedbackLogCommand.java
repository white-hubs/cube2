package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 13:47
 * @Description:
 */

@Data
public class TaskFeedbackLogCommand {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 任务节点编码
     */
    private String taskNodeKey;

    /**
     * 反馈信息
     */
    private String feedbackInfo;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 照片集合
     */

    private List<Long> fileIdList;
}
