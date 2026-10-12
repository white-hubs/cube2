package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/10/25 15:08
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfoDTO {
    private String id;

    private String flowKey;
    private String processInstanceId;
    private String name;
    private LocalDateTime submitTime;
    private String processStatus;
}
