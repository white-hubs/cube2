package com.guhecloud.cube.base.flow.flowable.task.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 11:22
 * @Description: 任务反馈记录表
 */
@Data
@Builder
@TableName("task_feedback_log")
@EqualsAndHashCode(callSuper = false)
public class TaskFeedbackLogPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;

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
     * 反馈人
     */
    private Long feedbackUser;
}
