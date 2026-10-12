package com.guhecloud.cube.base.flow.flowable.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserTaskSimpleDTO {

    private String id;
    private String key;
    private String name;
    private ProcessUserDTO assigneeUser;
    private List<ProcessUserDTO> candidateUsers;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime claimTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private TaskAttribute attr;

    private List<String> outcomes;

}
