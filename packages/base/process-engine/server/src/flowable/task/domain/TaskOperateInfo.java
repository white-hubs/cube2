package com.guhecloud.cube.base.flow.flowable.task.domain;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/10/31 13:46
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskOperateInfo {
    private String taskId;
    private String result;
    private String operator;
    private LocalDateTime completedTime;
    private String opinionText;
    private ApprovalOpinion opinion;
}
