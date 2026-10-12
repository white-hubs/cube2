package com.guhecloud.cube.base.flow.flowable.task.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CurrentUserTaskDTO {

    private String id;
    private String key;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime claimTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String formKey;

    private ProcessUserDTO assigneeUser;
    private List<ProcessUserDTO> candidateUsers;

    private ProcessInstanceSimpleDTO processInstance;

}
