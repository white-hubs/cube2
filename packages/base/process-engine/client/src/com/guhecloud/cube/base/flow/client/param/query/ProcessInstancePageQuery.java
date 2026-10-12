package com.guhecloud.cube.base.flow.client.param.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
public class ProcessInstancePageQuery extends Paging {
    private String type;
    @NotBlank
    private String businessType;
    private String status;
    private Long startUserId;
    private String username;
    private Long organizationId;
    private Period submitTime;
    private Period completionTime;
    private String applicationCode;
    private String processInstanceIds;
    private List<String> processInstanceIdsList;

    /**
     * 当前活跃任务名
     */
    private String currentTaskName;
}
