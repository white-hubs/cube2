package com.guhecloud.cube.base.flow.client.dto;

import com.guhecloud.cube.base.flow.client.ProcessInstanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HistoricalProcessInstanceDTO {
    /**
     * 流程实例id
     */
    private String id;

    /**
     * 流程状态
     */
    private ProcessInstanceStatus processStatus;

    /**
     * 发起时间
     */
    private String submittedTime;

    /**
     * 发起人
     */
    private Long submittedUserId;
    /**
     * 当前活跃节点，流程状态为完成时该值为空
     */
    private String activatedNodeName;

    /**
     * 用户变量
     */
    private Map<String, Object> variables;
}
