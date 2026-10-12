package com.guhecloud.cube.base.flow.flowable.task.application.dto;

import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import lombok.Data;

@Data
public class ProcessInstanceSimpleDTO {

    private String id;
    private String key;
    private String name;
    private Integer version;
    private String tenantId;

    private String businessType;
    private String businessKey;
    private ProcessUserDTO startUser;

}
