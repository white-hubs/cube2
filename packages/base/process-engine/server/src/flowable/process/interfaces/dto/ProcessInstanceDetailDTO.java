package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInstanceDetailDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/11/11 16:13
 * @description
 */
@Data
@Builder
public class ProcessInstanceDetailDTO {
    private String id;
    private String flowKey;
    private String data;
    private String status;
    private String remark;
    private Long startUserId;
    private LocalDateTime startTime;
    private TaskInstanceDetailDTO currentTask;
    private String formInstanceId;
    private String businessType;
}
