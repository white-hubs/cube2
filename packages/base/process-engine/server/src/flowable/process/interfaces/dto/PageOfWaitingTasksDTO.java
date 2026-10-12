package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageOfWaitingTasksDTO extends Paging {
    private Period taskCreateTime;
    private String businessType;
    private String applicationCode;
    private String status;

    public PageOfWaitingTasksDTO(Period taskCreateTime, String applicationCode, String businessType, String status, Integer pageNumber,
                                 Integer pageSize
                                ) {
        super(pageNumber, pageSize);
        this.taskCreateTime = taskCreateTime;
        this.applicationCode = applicationCode;
        this.status = status;
        this.businessType = businessType;
    }
}
